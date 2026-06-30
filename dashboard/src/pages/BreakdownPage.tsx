import type { ReactNode } from 'react'
import { Link, useParams } from 'react-router-dom'
import { useQuery } from '@tanstack/react-query'
import { AlertTriangle, ArrowLeft, Check, X } from 'lucide-react'

import { getResultBreakdown } from '../api/dashboard'
import type { BreakdownDto } from '../api/types'
import { Card } from '../components/ui/Card'
import { EmptyState } from '../components/ui/EmptyState'
import { Spinner } from '../components/ui/Spinner'
import { Badge } from '../components/ui/Badge'
import { AuthBadge } from '../components/domain/AuthBadge'
import { ScoreDisplay } from '../components/domain/ScoreDisplay'

export default function BreakdownPage() {
  const { id = '' } = useParams()

  const query = useQuery({
    queryKey: ['results', id, 'breakdown'],
    queryFn: () => getResultBreakdown(id),
  })

  return (
    <div className="space-y-5">
      <Link
        to="/results"
        className="inline-flex items-center gap-1 text-sm text-slate-500 transition-colors hover:text-navy"
      >
        <ArrowLeft size={15} /> Back to results
      </Link>

      <h2 className="font-display text-xl font-bold text-navy">Score breakdown</h2>

      {query.isPending ? (
        <Card>
          <Spinner block label="Loading breakdown…" />
        </Card>
      ) : query.isError ? (
        <Card>
          <EmptyState
            icon={AlertTriangle}
            title="Couldn't load this breakdown"
            description="It may not exist, or the backend is unreachable."
          />
        </Card>
      ) : (
        <Breakdown data={query.data} />
      )}
    </div>
  )
}

function Breakdown({ data }: { data: BreakdownDto }) {
  return (
    <div className="space-y-6">
      <Card title="Header">
        <div className="flex flex-wrap items-center gap-x-8 gap-y-4">
          <div>
            <p className="text-xs font-medium uppercase tracking-wide text-slate-400">Header score</p>
            <ScoreDisplay score={data.headScore} />
          </div>
          <div className="space-y-1.5">
            <p className="text-xs font-medium uppercase tracking-wide text-slate-400">Authentication</p>
            <div className="flex flex-wrap gap-2">
              <AuthBadge label="SPF" value={data.spfScore} />
              <AuthBadge label="DKIM" value={data.dkimScore} />
              <AuthBadge label="DMARC" value={data.dmarcScore} />
            </div>
          </div>
          <div className="space-y-1.5">
            <p className="text-xs font-medium uppercase tracking-wide text-slate-400">Identity match</p>
            <div className="flex flex-wrap gap-2">
              <MatchBadge label="Reply-To" value={data.replyToMatch} />
              <MatchBadge label="Return-Path" value={data.retPathMatch} />
            </div>
          </div>
        </div>
      </Card>

      <div className="grid gap-6 lg:grid-cols-2">
        <Card title={`URL scores (${data.urls.length})`}>
          <ScoreList
            items={data.urls.map((u) => ({ key: u.linkUrl, score: u.linkScore, label: u.linkUrl }))}
            emptyText="No URLs scored."
          />
        </Card>

        <Card title={`Attachment scores (${data.attachments.length})`}>
          <ScoreList
            items={data.attachments.map((a) => ({
              key: a.attName,
              score: a.attScore,
              label: <span className="font-mono text-xs">{a.attName}</span>,
            }))}
            emptyText="No attachments scored."
          />
        </Card>
      </div>
    </div>
  )
}

function MatchBadge({ label, value }: { label: string; value: boolean }) {
  return (
    <Badge className={value ? 'bg-safe/10 text-safe' : 'bg-slate-100 text-slate-500'}>
      {value ? <Check size={13} /> : <X size={13} />}
      {label} {value ? 'match' : 'mismatch'}
    </Badge>
  )
}

interface ScoreItem {
  key: string
  label: ReactNode
  score: number
}

function ScoreList({ items, emptyText }: { items: ScoreItem[]; emptyText: string }) {
  if (items.length === 0) {
    return <p className="text-sm text-slate-400">{emptyText}</p>
  }

  const sorted = [...items].sort((a, b) => b.score - a.score)
  const max = Math.max(...sorted.map((i) => i.score), 0)

  return (
    <ul className="space-y-3">
      {sorted.map((item) => {
        const pct = max > 0 ? Math.round((item.score / max) * 100) : 0
        return (
          <li key={item.key} className="space-y-1">
            <div className="flex items-baseline justify-between gap-3 text-sm">
              <span className="min-w-0 break-all text-slate-700" title={item.key}>
                {item.label}
              </span>
              <span className="shrink-0 font-display font-semibold tabular-nums text-navy">
                {item.score}
              </span>
            </div>
            <div className="h-1.5 w-full rounded-full bg-slate-100">
              <div className="h-full rounded-full bg-primary" style={{ width: `${pct}%` }} />
            </div>
          </li>
        )
      })}
    </ul>
  )
}
