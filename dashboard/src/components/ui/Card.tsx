import type { ReactNode } from 'react'

interface CardProps {
  title?: ReactNode
  action?: ReactNode
  children: ReactNode
  className?: string
}

export function Card({ title, action, children, className = '' }: CardProps) {
  return (
    <section className={`rounded-xl border border-slate-200 bg-surface shadow-sm ${className}`}>
      {title != null && (
        <header className="flex items-center justify-between gap-3 border-b border-slate-100 px-5 py-3">
          <h3 className="font-display text-sm font-semibold text-navy">{title}</h3>
          {action}
        </header>
      )}
      <div className="p-5">{children}</div>
    </section>
  )
}
