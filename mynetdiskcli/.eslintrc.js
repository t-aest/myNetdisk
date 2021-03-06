// https://eslint.org/docs/user-guide/configuring

module.exports = {
  root: true,
  parserOptions: {
    parser: 'babel-eslint'
  },
  env: {
    browser: true,
    // 添加
    jquery: true
  },
  extends: [
    // https://github.com/vuejs/eslint-plugin-vue#priority-a-essential-error-prevention
    // consider switching to `plugin:vue/strongly-recommended` or `plugin:vue/recommended` for stricter rules.
    'plugin:vue/essential',
    // https://github.com/standard/standard/blob/master/docs/RULES-en.md
    'standard'
  ],
  // required to lint *.vue files
  plugins: [
    'vue'
  ],
  // add your custom rules here
  rules: {
    // allow async-await
    // 'generator-star-spacing': 'off',
    // // allow debugger during development
    // 'no-debugger': process.env.NODE_ENV === 'production' ? 'error' : 'off',
    // 'no-undef': 'off',
    // 'vue/no-unused-vars': 'off',
    // 'vue/require-v-for-key': 'off',
    // 'no-unused-vars': 'off',
    // 'vue/no-unused-components': 'off',
    // "no-tabs":"off",
    // 'semi': 0,
    // "indent": ["off", 2],
    // "space-before-function-paren": 0
  }
}
