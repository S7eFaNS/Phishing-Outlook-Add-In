import { useSearchParams } from 'react-router-dom'

export const PAGE_SIZE = 20

export function usePageParam(): [apiPage: number, goToPage: (page: number) => void] {
  const [searchParams, setSearchParams] = useSearchParams()
  const apiPage = Math.max(0, (Number(searchParams.get('page')) || 1) - 1)

  const goToPage = (next: number) => {
    setSearchParams((prev) => {
      const params = new URLSearchParams(prev)
      params.set('page', String(next + 1))
      return params
    })
  }

  return [apiPage, goToPage]
}
