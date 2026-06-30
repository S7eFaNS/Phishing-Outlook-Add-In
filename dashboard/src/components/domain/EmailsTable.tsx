import { useNavigate } from 'react-router-dom'
import type { UseQueryResult } from '@tanstack/react-query'
import { AlertTriangle, ChevronRight } from 'lucide-react'

import type { EmailDto, Page } from '../../api/types'
import { formatDateTime } from '../../lib/format'
import { Card } from '../ui/Card'
import { EmptyState } from '../ui/EmptyState'
import { Spinner } from '../ui/Spinner'
import { Pagination } from '../ui/Pagination'
import { Table } from '../ui/Table'
import type { Column } from '../ui/Table'

const columns: Column<EmailDto>[] = [
  {
    header: 'From',
    cell: (e) => (
      <span className="block max-w-[15rem] truncate" title={e.phFrom}>
        {e.phFrom}
      </span>
    ),
  },
  {
    header: 'Recipient',
    cell: (e) => (
      <span className="block max-w-[13rem] truncate" title={e.rcpt}>
        {e.rcpt}
      </span>
    ),
  },
  {
    header: 'Subject',
    cell: (e) => (
      <span className="block max-w-[22rem] truncate font-medium text-slate-800" title={e.sub}>
        {e.sub}
      </span>
    ),
  },
  {
    header: 'Received',
    className: 'w-px whitespace-nowrap',
    cell: (e) => <span className="text-slate-500">{formatDateTime(e.timestampMail)}</span>,
  },
  {
    header: '',
    className: 'w-px whitespace-nowrap text-right',
    cell: () => (
      <span className="inline-flex items-center gap-0.5 font-medium text-primary">
        Details <ChevronRight size={14} />
      </span>
    ),
  },
]

interface EmailsTableProps {
  query: UseQueryResult<Page<EmailDto>>
  onPageChange: (page: number) => void
  emptyTitle?: string
  emptyDescription?: string
}


export function EmailsTable({
  query,
  onPageChange,
  emptyTitle = 'No emails',
  emptyDescription,
}: EmailsTableProps) {
  const navigate = useNavigate()

  if (query.isPending) {
    return (
      <Card>
        <Spinner block label="Loading emails…" />
      </Card>
    )
  }

  if (query.isError) {
    return (
      <Card>
        <EmptyState
          icon={AlertTriangle}
          title="Couldn't load emails"
          description="Check the backend connection and try again."
        />
      </Card>
    )
  }

  const page = query.data
  if (page.content.length === 0) {
    return (
      <Card>
        <EmptyState title={emptyTitle} description={emptyDescription} />
      </Card>
    )
  }

  return (
    <div className="overflow-hidden rounded-xl border border-slate-200 bg-surface shadow-sm">
      <Table
        columns={columns}
        rows={page.content}
        rowKey={(e) => e.phMailId}
        onRowClick={(e) => navigate(`/emails/${e.phMailId}`)}
      />
      {page.totalPages > 1 && (
        <div className="border-t border-slate-100 px-4 py-2">
          <Pagination
            page={page.number}
            totalPages={page.totalPages}
            totalElements={page.totalElements}
            onPageChange={onPageChange}
          />
        </div>
      )}
    </div>
  )
}
