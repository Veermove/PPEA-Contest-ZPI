/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    './src/pages/**/*.{js,ts,jsx,tsx,mdx}',
    './src/components/**/*.{js,ts,jsx,tsx,mdx}',
    './src/app/**/*.{js,ts,jsx,tsx,mdx}',
  ],
  theme: {
      colors: {
        purple: "#502C68",
        gray: "#849799",
        lightgray: "#A1A8B3"
      }
  },
  plugins: [],
}
