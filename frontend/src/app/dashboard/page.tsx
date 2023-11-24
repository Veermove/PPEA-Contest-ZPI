'use client'

import { useTranslation } from '@/app/i18n/client';
import { useAuthContext } from '@/context/authContext';
import { redirect, useRouter } from 'next/navigation';
import { Container } from 'react-bootstrap';
import { FaCommentDots, FaRegCheckCircle, FaRegListAlt, FaRegTimesCircle, FaUserPlus } from 'react-icons/fa';
import { LuFolderEdit } from 'react-icons/lu';
import DoubleTile from './doubleTile';

function Dashboard() {
  const { t } = useTranslation('dashboard');
  const { user } = useAuthContext();
  const router = useRouter()

  const handleItemClick = (path: string) => {
    return router.push(`${path}`)
  }

  if (!user) {
    return redirect('/signin')
  }

  return (
    <Container className="py-3 mt-4">
      <DoubleTile
        firstTileProps={{
          text: t('projectSubmissions'),
          Icon: LuFolderEdit,
          onClick: () => handleItemClick("/assessor/submissions")
        }}
        secondTileProps={{
          text: t('initialRatings'),
          Icon: FaRegTimesCircle,
          onClick: () => handleItemClick("assessor/ratings/initial")
        }}
      />
      <DoubleTile
        firstTileProps={{
          text: t('PEMCriteria'),
          Icon: FaRegListAlt,
          onClick: () => handleItemClick("/")
        }}
        secondTileProps={{
          text: t('finalRatings'),
          Icon: FaRegCheckCircle,
          onClick: () => handleItemClick("/assessor/ratings/final")
        }}
      />
      <DoubleTile
        firstTileProps={{
          text: t('individualRatings'),
          Icon: FaUserPlus,
          onClick: () => handleItemClick("assessor/ratings/individual")
        }}
        secondTileProps={{
          text: t('studyVisits'),
          Icon: FaCommentDots,
          onClick: () => handleItemClick("/")
        }}
      />
    </Container>
  )
}

export default Dashboard;
