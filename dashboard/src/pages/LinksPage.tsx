import { useNavigate } from 'react-router-dom'
import { useQuery, keepPreviousData } from '@tanstack/react-query'
import { AlertTriangle, ChevronRight } from 'lucide-react'

import { getLinks } from '../api/dashboard'
import type { LinkDto } from '../api/types'
import { PAGE_SIZE, usePageParam } from '../lib/usePageParam'
import { Card } from '../components/ui/Card'
import { EmptyState } from '../components/ui/EmptyState'
import { Spinner } from '../components/ui/Spinner'
import { Pagination } from '../components/ui/Pagination'
import { Table } from '../components/ui/Table'
import type { Column } from '../components/ui/Table'

const columns: Column<LinkDto>[] = [
  {
    header: 'URL',
    cell: (l) => (
      <span className="block max-w-[40rem] truncate text-slate-700" title={l.linkUrl}>
        {l.linkUrl}
      </span>
    ),
  },
  {
    header: 'Count',
    className: 'w-px whitespace-nowrap tabular-nums',
    cell: (l) => l.count,
  },
  {
    header: '',
    className: 'w-px whitespace-nowrap text-right',
    cell: () => (
      <span className="inline-flex items-center gap-0.5 font-medium text-primary">
        Emails <ChevronRight size={14} />
      </span>
    ),
  },
]

export default function LinksPage() {
  const navigate = useNavigate()
  const [apiPage, goToPage] = usePageParam()

  const query = useQuery({
    queryKey: ['links', apiPage, PAGE_SIZE],
    queryFn: () => getLinks({ page: apiPage, size: PAGE_SIZE, sort: 'count,desc' }),
    placeholderData: keepPreviousData,
  })

  return (
    <div className="space-y-4">
      <div className="flex items-center justify-between">
        <h2 className="font-display text-xl font-bold text-navy">Links</h2>
        {query.data && (
          <span className="text-sm text-slate-400">{query.data.totalElements} total</span>
        )}
      </div>

      {query.isPending ? (
        <Card>
          <Spinner block label="Loading links…" />
        </Card>
      ) : query.isError ? (
        <Card>
          <EmptyState
            icon={AlertTriangle}
            title="Couldn't load links"
            description="Check the backend connection and try again."
          />
        </Card>
      ) : query.data.content.length === 0 ? (
        <Card>
          <EmptyState title="No links yet" description="URLs found in submissions will appear here." />
        </Card>
      ) : (
        <div className="overflow-hidden rounded-xl border border-slate-200 bg-surface shadow-sm">
          <Table
            columns={columns}
            rows={query.data.content}
            rowKey={(l) => l.linkLstId}
            onRowClick={(l) => navigate(`/links/${l.linkLstId}/emails`)}
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
