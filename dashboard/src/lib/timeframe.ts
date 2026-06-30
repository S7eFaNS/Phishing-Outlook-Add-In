export type TimeframeKey = 'week' | 'month' | 'month3' | 'month6' | 'month12' | 'all'

export interface Timeframe {
  key: TimeframeKey
  label: string
}

export const TIMEFRAMES: Timeframe[] = [
  { key: 'week', label: 'Last week' },
  { key: 'month', label: 'Last month' },
  { key: 'month3', label: 'Last 3 months' },
  { key: 'month6', label: 'Last 6 months' },
  { key: 'month12', label: 'Last 12 months' },
  { key: 'all', label: 'All time' },
]

export const DEFAULT_TIMEFRAME: TimeframeKey = 'month'

export function timeframeLabel(key: TimeframeKey): string {
  return TIMEFRAMES.find((t) => t.key === key)?.label ?? key
}

export interface InstantRange {
  from: string
  to: string
}

export function timeframeRange(key: TimeframeKey, now: Date = new Date()): InstantRange {
  const to = now.toISOString()
  if (key === 'all') return { from: new Date(0).toISOString(), to }

  const from = new Date(now)
  switch (key) {
    case 'week':
      from.setDate(from.getDate() - 7)
      break
    case 'month':
      from.setMonth(from.getMonth() - 1)
      break
    case 'month3':
      from.setMonth(from.getMonth() - 3)
      break
    case 'month6':
      from.setMonth(from.getMonth() - 6)
      break
    case 'month12':
      from.setFullYear(from.getFullYear() - 1)
      break
  }
  return { from: from.toISOString(), to }
}
