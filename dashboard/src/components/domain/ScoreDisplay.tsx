import type { Level } from '../../api/types'
import { levelTextClass } from '../../lib/levels'

interface ScoreDisplayProps {
  score: number
  level?: Level
  size?: 'sm' | 'lg'
}

export function ScoreDisplay({ score, level, size = 'lg' }: ScoreDisplayProps) {
  const colour = level ? levelTextClass[level] : 'text-navy'
  const text = size === 'lg' ? 'text-3xl' : 'text-xl'
  return (
    <span className={`font-display font-bold tabular-nums ${text} ${colour}`}>
      {score}
      <span className="ml-1 text-sm font-medium text-slate-400">pts</span>
    </span>
  )
}
