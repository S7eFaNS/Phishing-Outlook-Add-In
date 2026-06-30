import { Routes, Route } from 'react-router-dom'

import { AppShell } from './components/layout/AppShell'
import { Card } from './components/ui/Card'
import { EmptyState } from './components/ui/EmptyState'

import OverviewPage from './pages/OverviewPage'
import EmailsPage from './pages/EmailsPage'
import EmailDetailPage from './pages/EmailDetailPage'
import LinksPage from './pages/LinksPage'
import LinkEmailsPage from './pages/LinkEmailsPage'
import AttachmentsPage from './pages/AttachmentsPage'
import AttachmentEmailsPage from './pages/AttachmentEmailsPage'
import ResultsPage from './pages/ResultsPage'
import BreakdownPage from './pages/BreakdownPage'

function NotFound() {
  return (
    <Card>
      <EmptyState title="Page not found" description="That route doesn't exist." />
    </Card>
  )
}

export default function App() {
  return (
    <Routes>
      <Route element={<AppShell />}>
        <Route path="/" element={<OverviewPage />} />
        <Route path="/emails" element={<EmailsPage />} />
        <Route path="/emails/:id" element={<EmailDetailPage />} />
        <Route path="/links" element={<LinksPage />} />
        <Route path="/links/:id/emails" element={<LinkEmailsPage />} />
        <Route path="/attachments" element={<AttachmentsPage />} />
        <Route path="/attachments/:id/emails" element={<AttachmentEmailsPage />} />
        <Route path="/results" element={<ResultsPage />} />
        <Route path="/results/:id/breakdown" element={<BreakdownPage />} />
        <Route path="*" element={<NotFound />} />
      </Route>
    </Routes>
  )
}
