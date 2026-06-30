import { Link, useParams } from 'react-router-dom'
import { useQuery, keepPreviousData } from '@tanstack/react-query'
import { ArrowLeft } from 'lucide-react'

import { getEmailsForAttachment } from '../api/dashboard'
import { PAGE_SIZE, usePageParam } from '../lib/usePageParam'
import { EmailsTable } from '../components/domain/EmailsTable'

export default function AttachmentEmailsPage() {
  const { id = '' } = useParams()
  const [apiPage, goToPage] = usePageParam()

  const query = useQuery({
    queryKey: ['attachments', id, 'emails', apiPage, PAGE_SIZE],
    queryFn: () => getEmailsForAttachment(id, { page: apiPage, size: PAGE_SIZE }),
    placeholderData: keepPreviousData,
  })

  return (
    <div className="space-y-4">
      <Link
        to="/attachments"
        className="inline-flex items-center gap-1 text-sm text-slate-500 transition-colors hover:text-navy"
      >
        <ArrowLeft size={15} /> Back to attachments
      </Link>

      <div className="flex items-center justify-between">
        <h2 className="font-display text-xl font-bold text-navy">Emails containing this attachment</h2>
        {query.data && (
          <span className="text-sm text-slate-400">{query.data.totalElements} total</span>
        )}
      </div>

      <EmailsTable
        query={query}
        onPageChange={goToPage}
        emptyTitle="No emails"
        emptyDescription="No submissions contained this attachment."
      />
    </div>
  )
}
