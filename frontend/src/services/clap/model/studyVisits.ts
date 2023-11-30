export interface StudyVisit {
    questions: Question[];
}

export interface Question {
    content: string;
    answers: Answer[];
}

interface Answer {
    answerText: string;
    files: string[];
}
