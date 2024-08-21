export interface Priority {
  label: string;
  value: number;
  color: Color
}
export type Color ="success" | "primary" | "secondary" | "danger" | "default" | "warning" | undefined

export const priorities: Priority[] = [
  { label: "Low", value: 0, color: "success" },
  { label: "Medium", value: 1, color: "primary" },
  { label: "High", value: 2, color: "warning" },
  { label: "Critical", value: 3, color: "danger" }
];
