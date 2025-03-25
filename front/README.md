# Monde de Dev
Welcome in Monde de Dev, an Angular Front-end !

This project uses Node v22.0.0, so before next step, think about using

```bash
nvm use
```
or
```bash
nvm install
```

Then, you can install your node_modules before starting (`npm install`).

## Start
Run `npm run start` for a dev server. The app will open on its onw with "-o" option but otherwise, you can navigate to `http://localhost:4200/`.

## Build
Run `ng build` to build the project. The build artifacts will be stored in the `dist/` directory.

## Front-end
This project has now only the Front end, in src file, there is the app with 3 folders :
- **app** : contains the basics of the app : app.component, guard, interceptor, the main routing file and the global layout
- **assets**: contains the images, icons, fonts, etc.
- **features**: contains the main features pages of the app
- **style**: contains the global style files including material theme
- **shared**: contains folders that can be user in other files like
  - components that can be reused
  - interface
  - services that can be called from feature pages
  - store
  - utils will also be placed here

## Style
Global style is made in style.scss fil but responsive and page style is made into component style files for now
The user screen must be minimum 320px.
Style is made mobile first and there is for now only one breakpoint that is :
- 768px

Material theme is used for the app, you can find the theme file in the style folder.
For documentation, you can go to https://material.angular.io/guide/theming

Using Material component is recommended, you can find the documentation here : https://material.angular.io/components/categories

According to the design, colors are defined as CSS global variables in the style.scss file.

## Back-end
Back-end code is in a parent repo, you nedd to run it to have the full app working.

## Clean code
This project uses prettier, to keep code clean you can use (`npm run prettier:write`)
This project uses eslint, to keep code clean you can use (`npm run lint`)

For an easy use, before commiting, please run : 
```bash
npm run prettier:write && npm run lint
```
