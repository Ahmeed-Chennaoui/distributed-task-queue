import type { Config } from "tailwindcss";
import { nextui } from "@nextui-org/react";
const config: Config = {
  content: [
    "./pages/**/*.{js,ts,jsx,tsx,mdx}",
    "./components/**/*.{js,ts,jsx,tsx,mdx}",
    "./app/**/*.{js,ts,jsx,tsx,mdx}",
    "./node_modules/@nextui-org/theme/dist/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      backgroundImage: {
        "gradient-radial": "radial-gradient(var(--tw-gradient-stops))",
        "gradient-conic":
          "conic-gradient(from 180deg at 50% 50%, var(--tw-gradient-stops))",
      },
      colors: {
        primary: "var(--primary)",
        secondary: "var(--secondary)",
        primaryText: "var(--primaryText)",
        secondaryText: "var(--secondaryText)",
      },
      fontSize: {
        h1: ["clamp(40px,8vw,60px)", "1.25"],
        h2: ["clamp(32px,6.4vw,48px)", "1.25"],
        h3: ["clamp(26px,5.2vw,39px)", "1.25"],
        h4: ["clamp(21px,4.2vw,31.5px)", "1.25"],
        h5: ["clamp(17px,3.4vw,25.5px)", "1.25"],
        h6: ["clamp(14px,2.8vw,21px)", "1.25"],
      },
      boxShadow: {
        insider:
          "rgba(50, 50, 93, 0.05) 0px 10px 30px -4px inset, rgba(0, 0, 0, 0.06) 0px 6px 12px -6px inset",
      },
    },
  },
  darkMode: "class",
  plugins: [nextui()],
};
export default config;
