
export function formatDate(dateStr: string): string {
    const date = new Date(dateStr);
    const formattedDate = date.toLocaleDateString("de-DE");

    return formattedDate
}

export function dateFromStr(dateStr: string): Date {
    return new Date(dateStr)
}