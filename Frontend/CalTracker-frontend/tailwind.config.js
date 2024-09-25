/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}",
  ],
  theme: {
    extend: {
      colors: {
        // Define the colors for the Soft Pastels palette
        beige: '#F8EDE3',
        'light-green': '#86A789',
        green: '#739072',
        'dark-green': '#4F6F52',
        'vibrant-green': '#80AF81'
      },
    },
  },
  plugins: [],
}