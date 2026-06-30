import { Check, Minus, X } from 'lucide-react'
import { Badge } from '../ui/Badge'

interface AuthBadgeProps {
  label: string
  value: boolean | string | null | undefined
}

type Status = 'pass' | 'fail' | 'unknown'

function statusOf(value: AuthBadgeProps['value']): Status {
  if (value == null) return 'unknown'
  if (typeof value === 'boolean') return value ? 'pass' : 'fail'
  return value.trim().toLowerCase() === 'pass' ? 'pass' : 'fail'
}

const STYLE: Record<Status, string> = {
  pass: 'bg-safe/10 text-safe',
  fail: 'bg-critical/10 text-critical',
  unknown: 'bg-slate-100 text-slate-500',
}

const ICON = { pass: Check, fail: X, unknown: Minus } as const

// SPF/DKIM/DMARC pass-fail badge, coloured green/red
export function AuthBadge({ label, value }: AuthBadgeProps) {
  const status = statusOf(value)
  const Icon = ICON[status]
  return (
    <Badge className={STYLE[status]}>
      <Icon size={13} />
      {label}
    </Badge>
  )
}
