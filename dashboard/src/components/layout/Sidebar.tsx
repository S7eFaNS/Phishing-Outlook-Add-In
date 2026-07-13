import { NavLink } from 'react-router-dom'
import type { LucideIcon } from 'lucide-react'
import { LayoutDashboard, Mail, Link2, Paperclip, ClipboardList } from 'lucide-react'

interface NavItem {
  to: string
  label: string
  icon: LucideIcon
  end?: boolean
}

const NAV: NavItem[] = [
  { to: '/', label: 'Overview', icon: LayoutDashboard, end: true },
  { to: '/emails', label: 'Emails', icon: Mail },
  { to: '/links', label: 'Links', icon: Link2 },
  { to: '/attachments', label: 'Attachments', icon: Paperclip },
  { to: '/results', label: 'Results', icon: ClipboardList },
]

export function Sidebar() {
  return (
    <aside className="flex w-60 shrink-0 flex-col border-r border-slate-200 bg-surface">
      <div className="flex flex-col items-center gap-2 px-5 py-5">
        <img src="/unicredit-bulbank.png" alt="UniCredit Bulbank" className="w-full" />
        <span className="font-display text-lg font-extrabold text-navy">PhishAid</span>
      </div>

      <nav className="flex flex-col gap-1 px-3">
        {NAV.map(({ to, label, icon: Icon, end }) => (
          <NavLink
            key={to}
            to={to}
            end={end}
            className={({ isActive }) =>
              `flex items-center gap-3 rounded-lg px-3 py-2 text-sm font-medium transition-colors ${
                isActive
                  ? 'bg-primary/10 text-primary'
                  : 'text-slate-600 hover:bg-page hover:text-navy'
              }`
            }
          >
            <Icon size={18} />
            {label}
          </NavLink>
        ))}
      </nav>
    </aside>
  )
}
