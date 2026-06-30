import type { Level } from '../api/types'

export const LEVELS: Level[] = ['SAFE', 'LOW', 'MEDIUM', 'HIGH', 'CRITICAL']

export const levelBadgeClass: Record<Level, string> = {
  SAFE: 'bg-safe/10 text-safe',
  LOW: 'bg-low/10 text-low',
  MEDIUM: 'bg-medium/10 text-medium',
  HIGH: 'bg-high/10 text-high',
  CRITICAL: 'bg-critical/10 text-critical',
}

export function levelLabel(level: Level): string {
  switch (level) {
    case 'SAFE':
      return 'Safe'
    case 'LOW':
      return 'Low'
    case 'MEDIUM':
      return 'Medium'
    case 'HIGH':
      return 'High'
    case 'CRITICAL':
      return 'Critical'
  }
}

export const levelTextClass: Record<Level, string> = {
  SAFE: 'text-safe',
  LOW: 'text-low',
  MEDIUM: 'text-medium',
  HIGH: 'text-high',
  CRITICAL: 'text-critical',
}

export const levelColor: Record<Level, string> = {
  SAFE: '#16a34a',
  LOW: '#0ea5e9',
  MEDIUM: '#f59e0b',
  HIGH: '#f97316',
  CRITICAL: '#dc2626',
}

const INCOMPLETE_SUFFIX = ' (incomplete)'

export interface ParsedLevel {
  level: Level
  incomplete: boolean
}

export function parseAnalysDesc(desc: string): ParsedLevel {
  const incomplete = desc.endsWith(INCOMPLETE_SUFFIX)
  const base = (incomplete ? desc.slice(0, -INCOMPLETE_SUFFIX.length) : desc).trim().toUpperCase()
  const level = (LEVELS as string[]).includes(base) ? (base as Level) : 'SAFE'
  return { level, incomplete }
}
