import { Color } from "./priorities"

export type State = "Completed"|"Processing"|"Pending"|"Failed"|"Queued"|"TimedOut"|"Aborted"
export interface StateColor {
    state : State,
    color : Color
}
export const stateColors : Record<State,Color> = {
    "Pending": "default",
    "Aborted": "danger",
    "Completed": "success",
    "Failed": "danger",
    "Processing": "secondary",
    "TimedOut": "danger",
    "Queued": "secondary"
}