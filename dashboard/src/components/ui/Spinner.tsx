import { Loader2 } from 'lucide-react'

interface SpinnerProps {
  size?: number
  block?: boolean
  label?: string
}

export function Spinner({ size = 18, block = false, label }: SpinnerProps) {
  const icon = (
    <span className="inline-flex items-center gap-2 text-slate-400">
      <Loader2 size={size} className="animate-spin" />
      {label && <span className="text-sm">{label}</span>}
    </span>
  )

  if (!block) return icon
  return <div className="flex items-center justify-center py-12">{icon}</div>
}
