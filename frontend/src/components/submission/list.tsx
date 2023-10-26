import { useTranslation } from "@/app/i18n/client";
import { RatingType } from "@/services/clap/model/rating";
import { SubmissionDTO } from "@/services/clap/model/submission";
import { Container, Row } from "react-bootstrap";
import SubmissionOverview from "./overview";

function splitSubmissionList(submissionList: SubmissionDTO[]): { activeSubmissions: SubmissionDTO[], archiveSubmissions: SubmissionDTO[] } {
    return submissionList.reduce((result: { activeSubmissions: SubmissionDTO[], archiveSubmissions: SubmissionDTO[] }, submission) => {
        result[submission.ratings.filter(rating => rating.type === RatingType.FINAL && rating.isDraft === false).length > 0 ? 'archiveSubmissions' : 'activeSubmissions'].push(submission)
        return result;
    }, { activeSubmissions: [], archiveSubmissions: [] })
}

function SubmissionList({ submissionList }: { submissionList: SubmissionDTO[] }) {

    const {t} = useTranslation('submissionList');

    const { activeSubmissions, archiveSubmissions } = splitSubmissionList(submissionList);
    return (
        <Container>
            <Row>
                {!!activeSubmissions.length && (
                    <>
                        <h3>{t('activeSubmission')}</h3>
                        <SubmissionOverview submission={activeSubmissions[0]} isActive={true} />
                    </>
                )}
            </Row>
            <Row>
                {!!archiveSubmissions.length && (
                    <>
                        <h3>{t('archiveSubmissions')}</h3>
                        {archiveSubmissions.map(submission => <SubmissionOverview submission={submission} isActive={false} />)}
                    </>
                )}
            </Row>
        </Container>
    )
}

export default SubmissionList;
