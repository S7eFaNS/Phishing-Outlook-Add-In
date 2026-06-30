import { Badge } from '../ui/Badge'
import { levelBadgeClass, parseAnalysDesc } from '../../lib/levels'

interface ThreatLevelBadgeProps {
  analysDesc: string
}

export function ThreatLevelBadge({ analysDesc }: ThreatLevelBadgeProps) {
  const { level, incomplete } = parseAnalysDesc(analysDesc)
  return (
    <span className="inline-flex items-center gap-1.5">
      <Badge className={levelBadgeClass[level]}>{level}</Badge>
      {incomplete && <Badge>incomplete</Badge>}
    </span>
  )
}
