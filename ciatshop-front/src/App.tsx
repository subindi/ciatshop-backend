import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import ItemListPage from './pages/ItemListPage';
import ItemDetailPage from './pages/ItemDetailPage';
import EventSelectPage from './pages/EventSelectPage';
import DateSelectPage from './pages/DateSelectPage';
import SalesCounterPage from './pages/SalesCounterPage';

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Navigate to="/counter" replace />} />
        <Route path="/counter" element={<EventSelectPage />} />
        <Route path="/counter/:eventSeq" element={<DateSelectPage />} />
        <Route path="/counter/:eventSeq/:ymd" element={<SalesCounterPage />} />
        <Route path="/items" element={<ItemListPage />} />
        <Route path="/items/:itemCd" element={<ItemDetailPage />} />
      </Routes>
    </BrowserRouter>
  );
}
