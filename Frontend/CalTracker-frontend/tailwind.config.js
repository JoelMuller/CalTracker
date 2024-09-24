/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}",
  ],
  theme: {
    extend: {
      colors: {
        // Define the colors for the Soft Pastels palette
        peach: '#F4F1DE',
        mint: '#2A9D8F',
        navy: '#264653',
        'light-navy': '#5C677D',  // Optional: A lighter version of navy if needed
        cream: '#FFFDF4',  // A lighter background color
        gold: '#E9C46A', // Optional: For accents or special highlights
      },
    },
  },
  plugins: [],
}