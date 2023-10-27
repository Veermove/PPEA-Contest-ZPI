import { PartialRating, Rating } from "./rating";

export interface Assesor {
    assesorId: number;
    personId: number;
    firstName: string;
    lastName: string;
    email: string;
}

export interface ApplicationReport {
    applicationReportId: number;
    isDraft: boolean;
    reportSubmissionDate?: Date;
    projectGoals?: string;
    organisationStructure?: string;
    divisionOfWork?: string;
    projectSchedule?: string;
    attachements?: string;
}

interface ContestDTO {
    contestId: number;
    year: number;
}

export interface SubmissionDTO {
    submissionId: number;
    name: string;
    durationDays: string;
    assesors: Assesor[];
    ratings: Rating[];
    contest: ContestDTO;
}

export interface SubmissionDetailsDTO {
    submissionId: number;
    name: string;
    durationDays: string;
    teamSize: number;
    finishDate: Date;
    budget: string;
    description: string;
    report?: ApplicationReport;
}

export interface AssessorsRatings {
    firstName: string;
    lastName: string;
    partialRatings: PartialRating[];
}

