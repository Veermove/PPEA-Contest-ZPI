'use client'
import { Col, Container, Row } from 'react-bootstrap';
import { FaRegTimesCircle, FaRegListAlt, FaUserPlus, FaRegCheckCircle, FaCommentDots } from 'react-icons/fa';
import { LuFolderEdit } from 'react-icons/lu';
import { useTranslation } from '@/app/i18n/client';
import { useParams, useRouter } from 'next/navigation';
import { useAuthContext } from '@/context/authContext';
import "./page.css"

function Dashboard() {
  const { locale } = useParams();
  const lang = typeof locale === 'object' ? locale[0] : locale
  const { t } = useTranslation(lang, 'dashboard');
  const { user } = useAuthContext();
  const router = useRouter()

  if (!user) {
    return router.push(`/${lang}`)
  }

  const handleItemClick = (path: string) => {
    return router.push(`/${lang}/${path}`)
  }

  return (
    <Container className="py-3 mt-4">
      <Row className="my-2 p-3">
        <Col xs={6} className="btn btn-light btn-transparent-background">
          <Row className="justify-content-around">
            <Col xs={4}>
              <div className="d-flex align-items-center h-100">
                <h3 align="center">{t('projectSubmissions')}</h3>
              </div>
            </Col>
            <Col xs={2}>
              <LuFolderEdit size={72} />
            </Col>
          </Row>
        </Col>
        <Col xs={6} className="btn btn-light btn-transparent-background">
          <Row className="justify-content-around">
            <Col xs={4}>
              <div className="d-flex align-items-center h-100">
                <h3 align="center">{t('initialRatings')}</h3>
              </div>
            </Col>
            <Col xs={2}>
              <FaRegTimesCircle size={72} />
            </Col>
          </Row>
        </Col>
      </Row>
      <Row className="my-2 p-3">
        <Col xs={6} className="btn btn-light btn-transparent-background">
          <Row className="justify-content-around">
            <Col xs={4}>
              <div className="d-flex align-items-center h-100">
                <h3 align="center">{t('PEMCriteria')}</h3>
              </div>
            </Col>
            <Col xs={2}>
              <FaRegListAlt size={72} />
            </Col>
          </Row>
        </Col>
        <Col xs={6} className="btn btn-light btn-transparent-background">
          <Row className="justify-content-around">
            <Col xs={4}>
              <div className="d-flex align-items-center h-100">
                <h3 align="center">{t('finalRatings')}</h3>
              </div>
            </Col>
            <Col xs={2}>
              <FaRegCheckCircle size={72} />
            </Col>
          </Row>
        </Col>
      </Row>
      <Row className="my-2 p-3">
        <Col xs={6} className="btn btn-light btn-transparent-background">
          <Row className="justify-content-around">
            <Col xs={4}>
              <div className="d-flex align-items-center h-100">
                <h3 align="center">{t('individualRatings')}</h3>
              </div>
            </Col>
            <Col xs={2}>
              <FaUserPlus size={72} />
            </Col>
          </Row>
        </Col>
        <Col xs={6} className="btn btn-light btn-transparent-background">
          <Row className="justify-content-around">
            <Col xs={4}>
              <div className="d-flex align-items-center h-100">
                <h3 align="center">{t('studyVisits')}</h3>
              </div>
            </Col>
            <Col xs={2}>
              <FaCommentDots size={72} />
            </Col>
          </Row>
        </Col>
      </Row>
    </Container>
  );
}

export default Dashboard;