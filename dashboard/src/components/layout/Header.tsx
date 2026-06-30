import type { ReactNode } from 'react'

interface HeaderProps {
  actions?: ReactNode
}

export function Header({ actions }: HeaderProps) {
  return (
    <header className="flex h-14 shrink-0 items-center justify-between border-b border-slate-200 bg-surface px-6">
      <h1 className="font-display text-base font-semibold text-navy">Phishing triage dashboard</h1>
      {actions ?? <span className="text-xs font-medium text-slate-400">CSOC · read-only</span>}
    </header>
  )
}
