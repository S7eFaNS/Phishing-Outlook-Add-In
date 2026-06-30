import type { ReactNode } from 'react'

export interface Column<T> {
  header: ReactNode
  cell: (row: T) => ReactNode
  className?: string
}

interface TableProps<T> {
  columns: Column<T>[]
  rows: T[]
  rowKey: (row: T) => string
  onRowClick?: (row: T) => void
}

export function Table<T>({ columns, rows, rowKey, onRowClick }: TableProps<T>) {
  return (
    <div className="overflow-x-auto">
      <table className="w-full border-collapse text-sm">
        <thead>
          <tr className="border-b border-slate-200 text-left">
            {columns.map((col, i) => (
              <th
                key={i}
                className={`px-4 py-2.5 font-display text-xs font-semibold uppercase tracking-wide text-slate-500 ${col.className ?? ''}`}
              >
                {col.header}
              </th>
            ))}
          </tr>
        </thead>
        <tbody>
          {rows.map((row) => (
            <tr
              key={rowKey(row)}
              onClick={onRowClick ? () => onRowClick(row) : undefined}
              className={`border-b border-slate-100 last:border-0 ${
                onRowClick ? 'cursor-pointer transition-colors hover:bg-page' : ''
              }`}
            >
              {columns.map((col, i) => (
                <td key={i} className={`px-4 py-3 text-slate-700 ${col.className ?? ''}`}>
                  {col.cell(row)}
                </td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}
