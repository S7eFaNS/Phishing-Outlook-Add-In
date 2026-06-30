# CLAUDE.md — PhishAid Dashboard (Frontend)

> Persistent context for Claude Code. Read fully before writing code.
> This is the **read-only React dashboard** that consumes the PhishAid backend's
> `/api/dashboard` endpoints. It performs **no writes** — every view is a GET.
> Goal: simple, clean, modern — not over-engineered. Prefer boring, readable components over
> clever abstractions.

---

## 1. What we are building

A single-page dashboard for CSOC analysts to review phishing submissions the backend has already
parsed and scored. Three jobs:
1. **Overview** — summary stats (totals, threat-level distribution, top sender domains/IPs).
2. **Browse + drill down** — paginated tables of emails / links / attachments / results, each with
   a detail view.
3. **Explain** — per-email composite detail and a per-result score breakdown ("why this score").

It is read-only and has no authentication (PoC, runs on a trusted internal network).

---

## 2. Tech stack

- **Vite + React + TypeScript.**
- **React Router** for routing.
- **TanStack Query (React Query)** for server state — handles the paginated GETs, caching,
  loading/error states. Don't hand-roll fetch-in-useEffect.
- **Tailwind CSS** for styling. No heavy component library; build the handful of primitives we need.
- **Recharts** for the stats charts.
- **lucide-react** for icons.
- A thin typed `fetch` wrapper for the API (no axios needed).

Keep dependencies to this list unless something is genuinely needed — resist scope creep.

---

## 3. Project structure

```
src/
├── api/
│   ├── client.ts          # typed fetch wrapper, base URL, error handling
│   ├── dashboard.ts       # one function per endpoint
│   └── types.ts           # TS types mirroring the backend DTOs (§5)
├── components/
│   ├── layout/            # AppShell, Sidebar, Header
│   ├── ui/                # Card, Table, Pagination, Badge, Spinner, EmptyState, StatCard
│   └── domain/            # ThreatLevelBadge, AuthBadge, ScoreDisplay
├── pages/
│   ├── OverviewPage.tsx
│   ├── EmailsPage.tsx
│   ├── EmailDetailPage.tsx
│   ├── LinksPage.tsx
│   ├── AttachmentsPage.tsx
│   ├── ResultsPage.tsx
│   └── BreakdownPage.tsx
├── lib/                   # formatters (dates, levels), constants
├── App.tsx                # routes
└── main.tsx               # React Query provider, router
```

---

## 4. Backend contract

- **Base URL:** all endpoints live under `/api/dashboard` (confirm against the controller's
  `@RequestMapping`). Configure via `VITE_API_BASE_URL` (default `http://localhost:8080`). In dev,
  set a Vite proxy so `/api` → backend, avoiding CORS.
- **Read-only:** only GET. No POST/PUT/DELETE anywhere.
- **Pagination:** list endpoints return a Spring `Page<T>` JSON: `{ content: T[], totalElements,
  totalPages, number, size, first, last }`. Send `?page=&size=&sort=` (e.g. `sort=count,desc`).
- **Verify field names before trusting these types.** The shapes in §5 are the expected contract
  from the backend spec, but the real JSON keys come from the Java DTOs. Before finalizing
  `types.ts`, `curl` one of each endpoint (or read the DTO classes) and match exactly — especially
  the nested shapes (`EmailDetailDto`, `BreakdownDto`).

### Endpoints
| Method + path | Returns |
|---|---|
| `GET /emails?page&size&sort` | `Page<EmailDto>` |
| `GET /emails/{id}` | `EmailDto` |
| `GET /emails/{id}/detail` | `EmailDetailDto` |
| `GET /emails/{id}/result` | `ResultDto` (404 if not yet analysed) |
| `GET /links?page&size&sort` | `Page<LinkDto>` |
| `GET /links/{id}/emails?page&size` | `Page<EmailDto>` |
| `GET /attachments?page&size&sort` | `Page<AttachmentDto>` |
| `GET /attachments/{id}/emails?page&size` | `Page<EmailDto>` |
| `GET /results?page&size&sort` | `Page<ResultDto>` |
| `GET /results/{id}` | `ResultDto` |
| `GET /results/{id}/breakdown` | `BreakdownDto` |
| `GET /stats/summary?from&to` | `SummaryDto` (ISO-8601 instants; default last 30 days) |
| `GET /stats/top-senders?limit` | `SenderCountDto[]` (default limit 10) |
| `GET /stats/top-sender-ips?limit` | `IpCountDto[]` |

---

## 5. Types (mirror the DTOs — verify against live responses)

```ts
type Page<T> = { content: T[]; totalElements: number; totalPages: number;
                 number: number; size: number; first: boolean; last: boolean };

type EmailDto = { phMailId: string; phFrom: string; rcpt: string; sub: string;
                  repTo: string | null; retPath: string | null; timestampMail: string };

type LinkDto = { linkLstId: string; linkUrl: string; count: number };
type AttachmentDto = { attLstId: string; attName: string; count: number }; // attName = SHA-256
type ResultDto = { analysRsltId: string; phMailId: string; totalScore: number;
                   analysDesc: string; frwdToHost: boolean }; // analysDesc may end " (incomplete)"

type RelayHopDto = { hopNumber: number; hopDescription: string };
type EmailDetailDto = EmailDto & {
  mailPath: { senderIp: string; spf: boolean; dkim: boolean; dmarc: boolean } | null;
  relay: RelayHopDto[];          // order by hopNumber
  links: LinkDto[];
  attachments: AttachmentDto[];
  result: ResultDto | null;      // null until analysed
};

type UrlScoreDto = { linkUrl: string; linkScore: number };
type AttScoreDto = { attName: string; attScore: number };
type BreakdownDto = {
  header: { headScore: number; spfScore: string; dkimScore: string; dmarcScore: string;
            replyToMatch: boolean; retPathMatch: boolean };
  urls: UrlScoreDto[];
  attachments: AttScoreDto[];
};

type SummaryDto = { totalEmails: number; totalResults: number;
                    levelCounts: Record<string, number>;   // { SAFE, LOW, MEDIUM, HIGH, CRITICAL }
                    frwdToHostCount: number; incompleteCount: number;
                    from: string; to: string };
type SenderCountDto = { senderDomain: string; count: number };
type IpCountDto = { senderIp: string; count: number };
```

---

## 6. Pages / routes

- `/` **Overview** — `StatCard`s (total emails, total results, broken-relay count, incomplete
  count), a threat-level distribution chart from `levelCounts`, and two horizontal bar charts
  (top senders, top sender IPs). One screen, scannable.
- `/emails` **Emails** — paginated table: From, Recipient, Subject, Received (timestampMail). Row →
  `/emails/:id`.
- `/emails/:id` **Email detail** — from `EmailDetailDto`: header card (from/to/subject/reply-to/
  return-path/received); auth card (sender IP + SPF/DKIM/DMARC via `AuthBadge`); relay chain
  (ordered hops); links list; attachments list; result card (`ScoreDisplay` + `ThreatLevelBadge`,
  or "not yet analysed"). If a result exists, link to its `/results/:id/breakdown`.
- `/links` **Links** — paginated table: URL, Count. Default sort `count,desc`. Row →
  `/links/:id/emails` (which submissions contained this URL).
- `/attachments` **Attachments** — table: Hash, Count. Default `count,desc`. Row →
  `/attachments/:id/emails`.
- `/results` **Results** — table: Score, Level (`ThreatLevelBadge`), Broken relay, Incomplete. Row →
  `/results/:id/breakdown`.
- `/results/:id/breakdown` **Breakdown** — header block (scores + SPF/DKIM/DMARC tokens + the two
  match flags), per-URL score list (now labelled with the actual URL), per-attachment score list.
  This is the "why this score" view.

Reusable list endpoints (`/links/:id/emails`, `/attachments/:id/emails`) render with the same emails
table component.

---

## 7. Design system

On-brand with the PhishAid presentation palette. Light theme, generous whitespace, restrained.

- **Colors:** primary/accent `#5E4CE6` (purple); headings/strong `#231971` (navy); page bg
  `#F4F3FA` (light); cards white with a soft border/shadow; dark surface `#2A2742` only if a dark
  panel is wanted. Neutral grays for text/borders.
- **Threat-level scale** (`ThreatLevelBadge`): SAFE → green, LOW → teal/blue, MEDIUM → amber,
  HIGH → orange, CRITICAL → red. Parse the base level out of `analysDesc` (strip a trailing
  " (incomplete)") and show a small "incomplete" tag separately when present.
- **Auth badges** (`AuthBadge`): pass → green check, fail → red. For breakdown tokens
  (`spfScore`/`dkimScore`/`dmarcScore` are strings like "pass"/"fail"), colour by value.
- **Typography:** headings `Outfit` (600/800); body `Inter` (system fallback). Load via the
  standard web-font link or `@fontsource`.
- **Layout:** left sidebar (nav: Overview, Emails, Links, Attachments, Results) + top header
  (title, maybe a date-range control for Overview) + content area.
- **Restraint:** no gradients-everywhere, no animations beyond subtle hover/transition, no glassmorphism.
  Clean cards, clear tables, readable charts. "Modern but not over the top" is the bar.

---

## 8. Conventions

- TypeScript everywhere; types in `api/types.ts` mirror the DTOs exactly.
- One API function per endpoint in `api/dashboard.ts`; components call them via React Query hooks
  (`useQuery`), keyed by endpoint + params.
- Every data view handles **loading** (spinner/skeleton), **error** (friendly message + the failing
  resource), and **empty** (`EmptyState`) states. An un-analysed email shows "not yet analysed",
  not an error.
- Dates: format `timestampMail` / summary window for humans (e.g. `date-fns` or `Intl`).
- Pagination state lives in the URL query string where practical, so views are linkable/back-button
  friendly.
- No auth, no writes, no global state library — React Query + local state is enough.
- `VITE_API_BASE_URL` for the backend origin; Vite dev proxy for `/api`.

---

## 9. Out of scope
- Any write/mutation. The `/api/dashboard/tickets` endpoint (not built on the backend).
- Authentication / login. Dark mode (unless trivial via tokens). Real-time updates / websockets.

If anything here conflicts with the actual backend responses, trust the live response and adjust the
types — then flag it.
