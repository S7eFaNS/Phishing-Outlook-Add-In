import { createContext, useContext, useEffect } from 'react'
import type { ReactNode } from 'react'


interface HeaderSlotApi {
  setActions: (node: ReactNode) => void
}

export const HeaderSlotContext = createContext<HeaderSlotApi>({ setActions: () => {} })

export function useHeaderActions(node: ReactNode): void {
  const { setActions } = useContext(HeaderSlotContext)
  useEffect(() => {
    setActions(node)
    return () => setActions(null)
  }, [node, setActions])
}
