const API_BASE_URL = (import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080').replace(/\/+$/, '')
const API_PREFIX = '/api/dashboard'

export class ApiError extends Error {
  readonly status: number
  readonly statusText: string
  readonly url: string
  readonly body?: string

  constructor(status: number, statusText: string, url: string, body?: string) {
    super(`API ${status || 'network'} ${statusText} — ${url}`)
    this.name = 'ApiError'
    this.status = status
    this.statusText = statusText
    this.url = url
    this.body = body
  }
}

type QueryValue = string | number | boolean | undefined | null
export type QueryParams = Record<string, QueryValue>

function buildUrl(path: string, params?: QueryParams): string {
  const url = `${API_BASE_URL}${API_PREFIX}${path}`
  if (!params) return url
  const search = new URLSearchParams()
  for (const [key, value] of Object.entries(params)) {
    if (value !== undefined && value !== null) search.append(key, String(value))
  }
  const qs = search.toString()
  return qs ? `${url}?${qs}` : url
}

export async function apiGet<T>(path: string, params?: QueryParams): Promise<T> {
  const url = buildUrl(path, params)

  let response: Response
  try {
    response = await fetch(url, { headers: { Accept: 'application/json' } })
  } catch (cause) {
    throw new ApiError(0, 'Network error', url, cause instanceof Error ? cause.message : String(cause))
  }

  if (!response.ok) {
    let body: string | undefined
    try {
      body = await response.text()
    } catch {
      body = undefined
    }
    throw new ApiError(response.status, response.statusText, url, body)
  }

  if (response.status === 204) return undefined as T
  return (await response.json()) as T
}

export interface PageParams {
  page?: number
  size?: number
  sort?: string // count,desc
}
