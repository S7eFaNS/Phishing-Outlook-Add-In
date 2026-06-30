import { useQuery, keepPreviousData } from '@tanstack/react-query'

import { getEmails } from '../api/dashboard'
import { PAGE_SIZE, usePageParam } from '../lib/usePageParam'
import { EmailsTable } from '../components/domain/EmailsTable'

export default function EmailsPage() {
  const [apiPage, goToPage] = usePageParam()

  const query = useQuery({
    queryKey: ['emails', apiPage, PAGE_SIZE],
    queryFn: () => getEmails({ page: apiPage, size: PAGE_SIZE }),
    placeholderData: keepPreviousData,
  })

  return (
    <div className="space-y-4">
      <div className="flex items-center justify-between">
        <h2 className="font-display text-xl font-bold text-navy">Emails</h2>
        {query.data && (
          <span className="text-sm text-slate-400">{query.data.totalElements} total</span>
        )}
      </div>

      <EmailsTable
        query={query}
        onPageChange={goToPage}
        emptyTitle="No emails yet"
        emptyDescription="Submitted phishing emails will appear here."
      />
    </div>
  )
}
