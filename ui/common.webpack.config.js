const merge = require('webpack-merge');
const path = require('path');
const webpack = require('webpack');

const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const OptimizeCSSAssetsPlugin = require('optimize-css-assets-webpack-plugin');
const UglifyJsPlugin = require('uglifyjs-webpack-plugin');

const generatedConfig = require('./scalajs.webpack.config');

const devMode = process.env.NODE_ENV !== 'production';

module.exports = merge(generatedConfig, {
  optimization: {
    minimizer: [
      new UglifyJsPlugin({
        cache: true,
        parallel: true,
        sourceMap: true
      }),
      new OptimizeCSSAssetsPlugin({})
    ]
  },
  plugins: [
    new MiniCssExtractPlugin({
      filename: devMode ? '[name].css' : '[name].[hash].css',
      chunkFilename: devMode ? '[id].css' : '[id].[hash].css'
    }),
    new webpack.ProvidePlugin({
      $: 'jquery',
      jQuery: 'jquery' // Bootstrap.js uses global jQuery internally
    })
  ],
  module: {
    rules: [
      {
        test: /\.(sa|sc|c)ss$/,
        use: [
          MiniCssExtractPlugin.loader,
          {
            // Translates CSS into CommonJS modules.
            loader: 'css-loader',
            options: {
              importLoaders: 2
            }
          },
          {
            // Run post css actions.
            loader: 'postcss-loader',
            options: {
              config: {
                // This is required otherwise postcss looks in the wrong place.
                path: __dirname
              }
            }
          },
          // Compiles Sass to CSS.
          'sass-loader'
        ]
      },
      {
        test: /\.(woff(2)?|ttf|eot|svg)$/,
        use: [
          {
            loader: 'file-loader',
            options: {}
          }
        ]
      }
    ]
  },
  resolve: {
    alias: {
      Stylesheets: '../../../../src/main/resources/stylesheets'
    },
    modules: [
      // This is required otherwise mini css extract plugin loads from the wrong place.
      path.resolve(__dirname, 'node_modules')
    ]
  }
});
