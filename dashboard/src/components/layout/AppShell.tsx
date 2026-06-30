import { useMemo, useState } from 'react'
import type { ReactNode } from 'react'
import { Outlet } from 'react-router-dom'
import { Sidebar } from './Sidebar'
import { Header } from './Header'
import { HeaderSlotContext } from './headerSlot'

export function AppShell() {
  const [actions, setActions] = useState<ReactNode>(null)
  const slot = useMemo(() => ({ setActions }), [])

  return (
    <HeaderSlotContext.Provider value={slot}>
      <div className="flex h-full">
        <Sidebar />
        <div className="flex min-w-0 flex-1 flex-col">
          <Header actions={actions} />
          <main className="flex-1 overflow-y-auto p-6">
            <Outlet />
          </main>
        </div>
      </div>
    </HeaderSlotContext.Provider>
  )
}
