# React Example UI

This project implements a simple UI with React.

Consult [`package.json`](./package.json) for appropriate scripts to *lint*, *start* and *build* the UI.

## Install

The project is managed with NPM, install dependencies with:

```shell
npm install
```

## Start (development server)

To start the application locally run

```shell
npm run start
```

### Environment variables

The UI requires access to a running version of the companion API ( version controlled at [`./api`](../api)) to function correctly. The UI sends HTTP requests to the API add will, obviously, need to know where the API is running. You can provide the UI with the location of the API by setting the `REACT_APP_API_BASE_URL` environment variable to the API's base URL. Typically, (and especially if you're following the primer for this system) the API will be running at <http://localhost:8080/react_example_api_war_exploded>.

On a Unix machine (with a value for `<url>` set appropriately):

```shell
export REACT_APP_API_BASE_URL=<url>
```
