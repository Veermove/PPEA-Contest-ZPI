This is a [Next.js](https://nextjs.org/) project bootstrapped with [`create-next-app`](https://github.com/vercel/next.js/tree/canary/packages/create-next-app).

## Getting Started

### Prerequisites
Install Node.JS 18 or higher. Please find the proper instructions for your operating system online.

### Install
Once you have your node installed, please run
```bash
npm i
```
to install all required npm packages.

### Environmental variables
This project utilizes Firebase as the Identity Provider. In order to set up the application properly, you need to add Firebase config to the environmental variables.
Copy the `env.template` file and rename to `.env`. Then, you can put the values of the secrets there. The `.env` file is gitignored. 

Then, you can run the development server:

```bash
npm run dev
# or
yarn dev
# or
pnpm dev
```

Open [http://localhost:3000](http://localhost:3000) with your browser to see the result.

This project uses [`next/font`](https://nextjs.org/docs/basic-features/font-optimization) to automatically optimize and load Inter, a custom Google Font.

### Docker
If you're not going to work on local development, but would like rather have this app working in a container, you can use Docker.
Make sure, as stated above, that you have the `.env` file prepared.
Then, run
```bash
docker build -t zpi-frontend .
docker run -p 3000:3000 -it zpi-frontend:latest
```

## Learn More

To learn more about Next.js, take a look at the following resources:

- [Next.js Documentation](https://nextjs.org/docs) - learn about Next.js features and API.
- [Learn Next.js](https://nextjs.org/learn) - an interactive Next.js tutorial.

You can check out [the Next.js GitHub repository](https://github.com/vercel/next.js/) - your feedback and contributions are welcome!
