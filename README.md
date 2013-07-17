release
=======

Read, learn and share books to unleash a storm of knowledge.

## Devs doc ##

### env config ###

- Install `grunt`: `npm install -g grunt-cli`
- Install `bower`: `npm install -g bower` (require version 0.10 or 1.0)
- Require Play! 2.1.1
- Using MongoDB 2.4.5 (might work on older versions)
- Require ReactiveMongo 0.9

### Run project ###

- Install / update dependencies with `grunt bower` each time `bower.json` file is edited.
- Build resources files with `grunt` each time your run the project.
- Use `sbt run` or `play run` to launch the server.