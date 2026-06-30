import type { ReactNode } from 'react'
import { Link, useParams } from 'react-router-dom'
import { useQuery } from '@tanstack/react-query'
import { AlertTriangle, ArrowLeft, ArrowRight, Clock } from 'lucide-react'

import { getEmailDetail } from '../api/dashboard'
import type { EmailDetailDto, ResultDto } from '../api/types'
import { formatDateTime, orDash } from '../lib/format'
import { parseAnalysDesc } from '../lib/levels'
import { Card } from '../components/ui/Card'
import { Badge } from '../components/ui/Badge'
import { EmptyState } from '../components/ui/EmptyState'
import { Spinner } from '../components/ui/Spinner'
import { AuthBadge } from '../components/domain/AuthBadge'
import { ScoreDisplay } from '../components/domain/ScoreDisplay'
import { ThreatLevelBadge } from '../components/domain/ThreatLevelBadge'

export default function EmailDetailPage() {
  const { id = '' } = useParams()

  const query = useQuery({
    queryKey: ['emails', id, 'detail'],
    queryFn: () => getEmailDetail(id),
  })

  return (
    <div className="space-y-5">
      <Link
        to="/emails"
        className="inline-flex items-center gap-1 text-sm text-slate-500 transition-colors hover:text-navy"
      >
        <ArrowLeft size={15} /> Back to emails
      </Link>

      {query.isPending ? (
        <Card>
          <Spinner block label="Loading email…" />
        </Card>
      ) : query.isError ? (
        <Card>
          <EmptyState
            icon={AlertTriangle}
            title="Couldn't load this email"
            description="It may not exist, or the backend is unreachable."
          />
        </Card>
      ) : (
        <EmailDetail detail={query.data} />
      )}
    </div>
  )
}

function EmailDetail({ detail }: { detail: EmailDetailDto }) {
  const relay = [...detail.relay].sort((a, b) => a.hopNumber - b.hopNumber)

  return (
    <div className="space-y-6">
      <Card title="Message">
        <dl className="grid gap-4 sm:grid-cols-2">
          <Field label="From">{detail.phFrom}</Field>
          <Field label="Recipient">{detail.rcpt}</Field>
          <Field label="Subject" wide>
            {detail.sub}
          </Field>
          <Field label="Reply-To">{orDash(detail.repTo)}</Field>
          <Field label="Return-Path">{orDash(detail.retPath)}</Field>
          <Field label="Received">{formatDateTime(detail.timestampMail)}</Field>
        </dl>
      </Card>

      <div className="grid gap-6 lg:grid-cols-2">
        <Card title="Authentication">
          <dl className="space-y-4">
            <Field label="Sender IP">{orDash(detail.senderIp)}</Field>
            <div>
              <dt className="mb-1.5 text-xs font-medium uppercase tracking-wide text-slate-400">
                Checks
              </dt>
              <dd className="flex flex-wrap gap-2">
                <AuthBadge label="SPF" value={detail.mailSpf} />
                <AuthBadge label="DKIM" value={detail.mailDkim} />
                <AuthBadge label="DMARC" value={detail.mailDmarc} />
              </dd>
            </div>
          </dl>
        </Card>

        <Card title="Result">
          <ResultCard result={detail.result} />
        </Card>

        <Card title="Relay chain">
          {relay.length === 0 ? (
            <p className="text-sm text-slate-400">No relay hops recorded.</p>
          ) : (
            <ol className="space-y-2">
              {relay.map((hop) => (
                <li key={hop.hopNumber} className="flex gap-3 text-sm">
                  <span className="font-display font-semibold text-slate-400">#{hop.hopNumber}</span>
                  <span className="break-words text-slate-700">{hop.hopDescription}</span>
                </li>
              ))}
            </ol>
          )}
        </Card>

        <Card title={`Links (${detail.links.length})`}>
          {detail.links.length === 0 ? (
            <p className="text-sm text-slate-400">No links found in this email.</p>
          ) : (
            <ul className="space-y-2">
              {detail.links.map((link) => (
                <li key={link.linkLstId} className="flex items-center justify-between gap-3 text-sm">
                  <span className="break-all text-slate-700">{link.linkUrl}</span>
                  <Badge>×{link.count}</Badge>
                </li>
              ))}
            </ul>
          )}
        </Card>

        <Card title={`Attachments (${detail.attachments.length})`}>
          {detail.attachments.length === 0 ? (
            <p className="text-sm text-slate-400">No inner attachments.</p>
          ) : (
            <ul className="space-y-2">
              {detail.attachments.map((att) => (
                <li key={att.attLstId} className="flex items-center justify-between gap-3 text-sm">
                  <span className="truncate font-mono text-xs text-slate-600" title={att.attName}>
                    {att.attName}
                  </span>
                  <Badge>×{att.count}</Badge>
                </li>
              ))}
            </ul>
          )}
        </Card>
      </div>
    </div>
  )
}

function ResultCard({ result }: { result: ResultDto | null }) {
  if (!result) {
    return (
      <EmptyState
        icon={Clock}
        title="Not yet analysed"
        description="This email hasn't been scored yet."
      />
    )
  }

  const { level } = parseAnalysDesc(result.analysDesc)
  return (
    <div className="space-y-4">
      <div className="flex items-center justify-between gap-3">
        <ScoreDisplay score={result.totalScore} level={level} />
        <ThreatLevelBadge analysDesc={result.analysDesc} />
      </div>
      <Link
        to={`/results/${result.analysRsltId}/breakdown`}
        className="inline-flex items-center gap-1 text-sm font-medium text-primary hover:underline"
      >
        View score breakdown <ArrowRight size={15} />
      </Link>
    </div>
  )
}

function Field({ label, children, wide }: { label: string; children: ReactNode; wide?: boolean }) {
  return (
    <div className={`flex flex-col gap-0.5 ${wide ? 'sm:col-span-2' : ''}`}>
      <dt className="text-xs font-medium uppercase tracking-wide text-slate-400">{label}</dt>
      <dd className="break-words text-sm text-slate-700">{children}</dd>
    </div>
  )
}
