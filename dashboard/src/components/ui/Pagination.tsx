import { ChevronLeft, ChevronRight } from 'lucide-react'

interface PaginationProps {
  page: number
  totalPages: number
  totalElements?: number
  onPageChange: (page: number) => void
}

export function Pagination({ page, totalPages, totalElements, onPageChange }: PaginationProps) {
  if (totalPages <= 1) return null

  const atStart = page <= 0
  const atEnd = page >= totalPages - 1
  const btn =
    'inline-flex items-center gap-1 rounded-lg border border-slate-200 px-3 py-1.5 text-sm font-medium text-slate-600 transition-colors enabled:hover:bg-page disabled:opacity-40'

  return (
    <div className="flex items-center justify-between gap-4 pt-3 text-sm text-slate-500">
      <span>
        Page {page + 1} of {totalPages}
        {totalElements != null && ` · ${totalElements} total`}
      </span>
      <div className="flex gap-2">
        <button className={btn} disabled={atStart} onClick={() => onPageChange(page - 1)}>
          <ChevronLeft size={16} /> Prev
        </button>
        <button className={btn} disabled={atEnd} onClick={() => onPageChange(page + 1)}>
          Next <ChevronRight size={16} />
        </button>
      </div>
    </div>
  )
}
