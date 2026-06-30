import { Link, useNavigate } from 'react-router-dom'
import { useQuery, keepPreviousData } from '@tanstack/react-query'
import { AlertTriangle, Check, ChevronRight, Minus } from 'lucide-react'

import { getResults } from '../api/dashboard'
import type { ResultDto } from '../api/types'
import { parseAnalysDesc } from '../lib/levels'
import { PAGE_SIZE, usePageParam } from '../lib/usePageParam'
import { Card } from '../components/ui/Card'
import { EmptyState } from '../components/ui/EmptyState'
import { Spinner } from '../components/ui/Spinner'
import { Pagination } from '../components/ui/Pagination'
import { Table } from '../components/ui/Table'
import type { Column } from '../components/ui/Table'
import { ThreatLevelBadge } from '../components/domain/ThreatLevelBadge'

function YesNo({ value, yesClass = 'text-slate-700' }: { value: boolean; yesClass?: string }) {
  return value ? (
    <span className={`inline-flex items-center gap-1 font-medium ${yesClass}`}>
      <Check size={14} /> Yes
    </span>
  ) : (
    <span className="inline-flex items-center gap-1 text-slate-400">
      <Minus size={14} /> No
    </span>
  )
}

const columns: Column<ResultDto>[] = [
  {
    header: 'Email',
    className: 'w-px whitespace-nowrap',
    cell: (r) => (
      <Link
        to={`/emails/${r.phMailId}`}
        onClick={(e) => e.stopPropagation()}
        className="font-mono text-xs text-primary hover:underline"
        title={r.phMailId}
      >
        {r.phMailId.slice(0, 8)}…
      </Link>
    ),
  },
  {
    header: 'Score',
    className: 'w-px whitespace-nowrap',
    cell: (r) => <span className="font-display font-semibold tabular-nums text-navy">{r.totalScore}</span>,
  },
  {
    header: 'Level',
    cell: (r) => <ThreatLevelBadge analysDesc={r.analysDesc} />,
  },
  {
    header: 'Header auth failed',
    className: 'w-px whitespace-nowrap',
    cell: (r) => <YesNo value={r.frwdToHost} yesClass="text-critical" />,
  },
  {
    header: 'Incomplete',
    className: 'w-px whitespace-nowrap',
    cell: (r) => <YesNo value={parseAnalysDesc(r.analysDesc).incomplete} yesClass="text-medium" />,
  },
  {
    header: '',
    className: 'w-px whitespace-nowrap text-right',
    cell: () => (
      <span className="inline-flex items-center gap-0.5 font-medium text-primary">
        Breakdown <ChevronRight size={14} />
      </span>
    ),
  },
]

export default function ResultsPage() {
  const navigate = useNavigate()
  const [apiPage, goToPage] = usePageParam()

  const query = useQuery({
    queryKey: ['results', apiPage, PAGE_SIZE],
    queryFn: () => getResults({ page: apiPage, size: PAGE_SIZE, sort: 'totalScore,desc' }),
    placeholderData: keepPreviousData,
  })

  return (
    <div className="space-y-4">
      <div className="flex items-center justify-between">
        <h2 className="font-display text-xl font-bold text-navy">Results</h2>
        {query.data && (
          <span className="text-sm text-slate-400">{query.data.totalElements} total</span>
        )}
      </div>

      {query.isPending ? (
        <Card>
          <Spinner block label="Loading results…" />
        </Card>
      ) : query.isError ? (
        <Card>
          <EmptyState
            icon={AlertTriangle}
            title="Couldn't load results"
            description="Check the backend connection and try again."
          />
        </Card>
      ) : query.data.content.length === 0 ? (
        <Card>
          <EmptyState title="No results yet" description="Scored submissions will appear here." />
        </Card>
      ) : (
        <div className="overflow-hidden rounded-xl border border-slate-200 bg-surface shadow-sm">
          <Table
            columns={columns}
            rows={query.data.content}
            rowKey={(r) => r.analysRsltId}
            onRowClick={(r) => navigate(`/results/${r.analysRsltId}/breakdown`)}
          />
          {query.data.totalPages > 1 && (
            <div className="border-t border-slate-100 px-4 py-2">
              <Pagination
                page={query.data.number}
                totalPages={query.data.totalPages}
                totalElements={query.data.totalElements}
                onPageChange={goToPage}
              />
            </div>
          )}
        </div>
      )}
    </div>
  )
}
