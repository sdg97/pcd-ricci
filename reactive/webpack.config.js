const HtmlWebpackPlugin = require('html-webpack-plugin');

module.exports = {
    mode: 'development',
    entry: ['babel-polyfill', './src/index.js'],
    output: {
        library: 'myFunction'
    },
    devServer: {
        contentBase: require('path').join(__dirname, 'dist'),
        compress: true,
        port: 5000, 
    },
    module: {

        rules: [{
            test: /\.css$/,
            use: ['style-loader', 'css-loader']
        }, {
            test: /\.(woff|woff2|eot|ttf|otf|svg)$/,
            use: 'file-loader'
        }, {
            test: /\.scss$/,
            use: ['sass-loader']
        }, {
            test: /\.js$/,
            exclude: /node_modules/,
            loader: 'babel-loader',
            query: {
                presets: ['@babel/preset-env']
            }
        }, {
            test: /\.ts$/,
            exclude: /node_modules/,
            loader: 'babel-loader',
            query: {
                presets: ['@babel/preset-typescript']
            }
        }]
    },

    plugins : [
        new HtmlWebpackPlugin({
            hash: true,
            filename: __dirname + '/dist/index.html', //relative to root of the application
            template: __dirname + '/index.html',
            inject: false
        })
    ]

}

