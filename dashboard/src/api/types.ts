export interface Page<T> {
  content: T[]
  totalElements: number
  totalPages: number
  number: number 
  size: number
  numberOfElements: number
  first: boolean
  last: boolean
  empty: boolean
}

export type Level = 'SAFE' | 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL'

export interface EmailDto {
  phMailId: string
  phFrom: string
  rcpt: string
  sub: string
  repTo: string | null
  retPath: string | null
  timestampMail: string 
}

export interface LinkDto {
  linkLstId: string
  linkUrl: string
  count: number
}

export interface AttachmentDto {
  attLstId: string
  attName: string 
  count: number
}

export interface ResultDto {
  analysRsltId: string
  phMailId: string
  totalScore: number
  analysDesc: string 
  frwdToHost: boolean
}

export interface RelayHopDto {
  hopNumber: number
  hopDescription: string
}

export type EmailDetailDto = EmailDto & {
  senderIp: string | null
  mailSpf: boolean | null
  mailDkim: boolean | null
  mailDmarc: boolean | null
  relay: RelayHopDto[]
  links: LinkDto[]
  attachments: AttachmentDto[]
  result: ResultDto | null
}

export interface UrlScoreDto {
  linkUrl: string
  linkScore: number
}

export interface AttScoreDto {
  attName: string
  attScore: number
}

export interface BreakdownDto {
  headScore: number
  spfScore: string 
  dkimScore: string
  dmarcScore: string
  replyToMatch: boolean
  retPathMatch: boolean
  urls: UrlScoreDto[]
  attachments: AttScoreDto[]
}

export interface SummaryDto {
  totalEmails: number
  totalResults: number
  levels: Record<Level, number> 
  frwdToHostCount: number
  incompleteCount: number
  from: string 
  to: string
}

export interface SenderCountDto {
  senderDomain: string
  count: number
}

export interface IpCountDto {
  senderIp: string
  count: number
}
