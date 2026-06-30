import { useMemo, useState } from 'react'
import type { ReactNode } from 'react'
import { useQuery } from '@tanstack/react-query'
import type { UseQueryResult } from '@tanstack/react-query'
import {
  Bar,
  BarChart,
  Cell,
  Pie,
  PieChart,
  ResponsiveContainer,
  Tooltip,
  XAxis,
  YAxis,
} from 'recharts'
import { AlertTriangle, CalendarRange } from 'lucide-react'

import { getSummary, getTopSenderIps, getTopSenders } from '../api/dashboard'
import type { IpCountDto, Level, SenderCountDto, SummaryDto } from '../api/types'
import { LEVELS, levelColor, levelLabel, levelTextClass } from '../lib/levels'
import {
  DEFAULT_TIMEFRAME,
  TIMEFRAMES,
  timeframeLabel,
  timeframeRange,
} from '../lib/timeframe'
import type { TimeframeKey } from '../lib/timeframe'
import { Card } from '../components/ui/Card'
import { EmptyState } from '../components/ui/EmptyState'
import { Spinner } from '../components/ui/Spinner'
import { useHeaderActions } from '../components/layout/headerSlot'

const BAR_FILL = '#5e4ce6' // primary (§7); Recharts needs a raw colour, not a class


export default function OverviewPage() {
  const [timeframe, setTimeframe] = useState<TimeframeKey>(DEFAULT_TIMEFRAME)

  // The selector lives in the shared header; memoise so the slot effect only
  // re-runs when the selected timeframe changes.
  const selector = useMemo(
    () => <TimeframeSelect value={timeframe} onChange={setTimeframe} />,
    [timeframe],
  )
  useHeaderActions(selector)

  // Resolve the window once per selection so the query key is stable across renders.
  const range = useMemo(() => timeframeRange(timeframe), [timeframe])

  // Only the summary follows the timeframe.
  const summaryQuery = useQuery({
    queryKey: ['stats', 'summary', range.from, range.to],
    queryFn: () => getSummary(range),
  })

  // Top senders / IPs are all-time: static keys → fetched once, never re-fetched
  // when the timeframe changes.
  const sendersQuery = useQuery({
    queryKey: ['stats', 'top-senders', 10],
    queryFn: () => getTopSenders(10),
  })
  const ipsQuery = useQuery({
    queryKey: ['stats', 'top-sender-ips', 10],
    queryFn: () => getTopSenderIps(10),
  })

  return (
    <div className="space-y-6">
      <Card
        title="Threat-level breakdown"
        action={
          <span className="text-xs font-medium text-slate-400">{timeframeLabel(timeframe)}</span>
        }
      >
        <Async
          query={summaryQuery}
          resource="summary stats"
          empty={(s) => totalCount(s) === 0}
          emptyText="No submissions in this window"
        >
          {(s) => <LevelBreakdown summary={s} />}
        </Async>
      </Card>

      <div className="grid gap-6 lg:grid-cols-2">
        <Card title="Top sender domains" action={<AllTimeTag />}>
          <Async
            query={sendersQuery}
            resource="top sender domains"
            empty={(d) => d.length === 0}
          >
            {(d: SenderCountDto[]) => (
              <HorizontalBars items={d.map((x) => ({ label: x.senderDomain, count: x.count }))} />
            )}
          </Async>
        </Card>

        <Card title="Top sender IPs" action={<AllTimeTag />}>
          <Async query={ipsQuery} resource="top sender IPs" empty={(d) => d.length === 0}>
            {(d: IpCountDto[]) => (
              <HorizontalBars items={d.map((x) => ({ label: x.senderIp, count: x.count }))} />
            )}
          </Async>
        </Card>
      </div>
    </div>
  )
}

// --- Timeframe selector (rendered into the header) ------------------------

function TimeframeSelect({
  value,
  onChange,
}: {
  value: TimeframeKey
  onChange: (key: TimeframeKey) => void
}) {
  return (
    <label className="flex items-center gap-2 rounded-lg border border-slate-200 bg-surface px-3 py-1.5 text-sm">
      <CalendarRange size={16} className="text-slate-400" />
      <select
        value={value}
        onChange={(e) => onChange(e.target.value as TimeframeKey)}
        className="cursor-pointer bg-transparent font-medium text-slate-700 focus:outline-none"
        aria-label="Summary timeframe"
      >
        {TIMEFRAMES.map((t) => (
          <option key={t.key} value={t.key}>
            {t.label}
          </option>
        ))}
      </select>
    </label>
  )
}

const AllTimeTag = () => (
  <span className="text-xs font-medium text-slate-400">All time</span>
)

// --- Async state wrapper (loading / error / empty) ------------------------

interface AsyncProps<T> {
  query: UseQueryResult<T>
  resource: string
  empty: (data: T) => boolean
  emptyText?: string
  children: (data: T) => ReactNode
}

function Async<T>({ query, resource, empty, emptyText, children }: AsyncProps<T>) {
  if (query.isPending) return <Spinner block label="Loading…" />
  if (query.isError) return <SectionError resource={resource} />
  const data = query.data
  if (empty(data)) return <EmptyState title={emptyText ?? `No ${resource}`} />
  return <>{children(data)}</>
}

function SectionError({ resource }: { resource: string }) {
  return (
    <div className="flex flex-col items-center gap-2 py-10 text-center">
      <AlertTriangle size={26} className="text-critical" />
      <p className="text-sm font-medium text-slate-700">Couldn&rsquo;t load {resource}</p>
      <p className="text-sm text-slate-400">Check the backend connection and try again.</p>
    </div>
  )
}

// --- Level breakdown (the headline) ---------------------------------------

function totalCount(summary: SummaryDto): number {
  return LEVELS.reduce((sum, level) => sum + (summary.levels[level] ?? 0), 0)
}

function LevelBreakdown({ summary }: { summary: SummaryDto }) {
  const total = totalCount(summary)
  const slices = LEVELS.map((level) => ({
    level,
    name: levelLabel(level),
    value: summary.levels[level] ?? 0,
  }))

  return (
    <div className="space-y-5">
      <div className="grid items-center gap-6 lg:grid-cols-[220px_1fr]">
        <div className="relative mx-auto h-[200px] w-[200px]">
          <ResponsiveContainer>
            <PieChart>
              <Pie
                data={slices}
                dataKey="value"
                nameKey="name"
                innerRadius={66}
                outerRadius={94}
                paddingAngle={2}
                strokeWidth={0}
              >
                {slices.map((s) => (
                  <Cell key={s.level} fill={levelColor[s.level]} />
                ))}
              </Pie>
              <Tooltip />
            </PieChart>
          </ResponsiveContainer>
          <div className="pointer-events-none absolute inset-0 flex flex-col items-center justify-center">
            <span className="font-display text-3xl font-bold text-navy">{total}</span>
            <span className="text-xs uppercase tracking-wide text-slate-400">Total</span>
          </div>
        </div>

        {/* One figure per level, plus a Total tile. */}
        <div className="grid grid-cols-2 gap-3 sm:grid-cols-3">
          {LEVELS.map((level) => (
            <LevelFigure
              key={level}
              level={level}
              count={summary.levels[level] ?? 0}
              total={total}
            />
          ))}
          <div className="rounded-lg border border-slate-200 bg-navy p-3 text-white">
            <span className="text-xs font-medium text-white/70">Total</span>
            <p className="mt-1 font-display text-2xl font-bold tabular-nums">{total}</p>
          </div>
        </div>
      </div>

      {/* Optional secondary stats — these also follow the timeframe. */}
      <div className="grid grid-cols-2 gap-3 border-t border-slate-100 pt-4 sm:max-w-md">
        <SmallStat label="Header auth failed" value={summary.frwdToHostCount} />
        <SmallStat label="Incomplete" value={summary.incompleteCount} />
      </div>
    </div>
  )
}

function LevelFigure({ level, count, total }: { level: Level; count: number; total: number }) {
  const pct = total > 0 ? Math.round((count / total) * 100) : 0
  return (
    <div className="rounded-lg border border-slate-100 bg-page/60 p-3">
      <div className="flex items-center gap-2">
        <span className="h-2.5 w-2.5 rounded-full" style={{ backgroundColor: levelColor[level] }} />
        <span className="text-xs font-medium text-slate-500">{levelLabel(level)}</span>
      </div>
      <p className={`mt-1 font-display text-2xl font-bold tabular-nums ${levelTextClass[level]}`}>
        {count}
      </p>
      <p className="text-xs text-slate-400">{pct}%</p>
    </div>
  )
}

function SmallStat({ label, value }: { label: string; value: number }) {
  return (
    <div className="rounded-lg border border-slate-100 bg-page/60 px-3 py-2">
      <p className="text-xs font-medium text-slate-500">{label}</p>
      <p className="mt-0.5 font-display text-xl font-bold tabular-nums text-navy">{value}</p>
    </div>
  )
}

//Horizontal bar chart (top senders / IPs) 

interface BarItem {
  label: string
  count: number
}

function HorizontalBars({ items }: { items: BarItem[] }) {
  const height = items.length * 34 + 8
  return (
    <div style={{ width: '100%', height }}>
      <ResponsiveContainer>
        <BarChart data={items} layout="vertical" margin={{ left: 0, right: 16, top: 0, bottom: 0 }}>
          <XAxis type="number" hide />
          <YAxis
            type="category"
            dataKey="label"
            width={150}
            tickLine={false}
            axisLine={false}
            tick={{ fontSize: 11, fill: '#64748b' }}
          />
          <Tooltip cursor={{ fill: '#f1f5f9' }} />
          <Bar dataKey="count" fill={BAR_FILL} radius={[0, 4, 4, 0]} barSize={16} />
        </BarChart>
      </ResponsiveContainer>
    </div>
  )
}
