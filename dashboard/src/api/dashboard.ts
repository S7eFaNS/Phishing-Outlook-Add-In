import { apiGet } from './client'
import type { PageParams, QueryParams } from './client'
import type {
  AttachmentDto,
  BreakdownDto,
  EmailDetailDto,
  EmailDto,
  IpCountDto,
  LinkDto,
  Page,
  ResultDto,
  SenderCountDto,
  SummaryDto,
} from './types'

const pageQuery = (params?: PageParams): QueryParams => ({
  page: params?.page,
  size: params?.size,
  sort: params?.sort,
})

//Emails
export const getEmails = (params?: PageParams) =>
  apiGet<Page<EmailDto>>('/emails', pageQuery(params))

export const getEmail = (id: string) => apiGet<EmailDto>(`/emails/${id}`)

export const getEmailDetail = (id: string) => apiGet<EmailDetailDto>(`/emails/${id}/detail`)

export const getEmailResult = (id: string) => apiGet<ResultDto>(`/emails/${id}/result`)

//Links 
export const getLinks = (params?: PageParams) =>
  apiGet<Page<LinkDto>>('/links', pageQuery(params))

export const getEmailsForLink = (id: string, params?: PageParams) =>
  apiGet<Page<EmailDto>>(`/links/${id}/emails`, pageQuery(params))

//Attachments 
export const getAttachments = (params?: PageParams) =>
  apiGet<Page<AttachmentDto>>('/attachments', pageQuery(params))

export const getEmailsForAttachment = (id: string, params?: PageParams) =>
  apiGet<Page<EmailDto>>(`/attachments/${id}/emails`, pageQuery(params))

//Results
export const getResults = (params?: PageParams) =>
  apiGet<Page<ResultDto>>('/results', pageQuery(params))

export const getResult = (id: string) => apiGet<ResultDto>(`/results/${id}`)

export const getResultBreakdown = (id: string) =>
  apiGet<BreakdownDto>(`/results/${id}/breakdown`)

//Stats
export const getSummary = (params?: { from?: string; to?: string }) =>
  apiGet<SummaryDto>('/stats/summary', params)

export const getTopSenders = (limit?: number) =>
  apiGet<SenderCountDto[]>('/stats/top-senders', { limit })

export const getTopSenderIps = (limit?: number) =>
  apiGet<IpCountDto[]>('/stats/top-sender-ips', { limit })
