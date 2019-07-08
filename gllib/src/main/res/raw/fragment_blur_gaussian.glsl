precision mediump float;
uniform sampler2D sourceImage;
varying vec2 textureCoordinate[13];

void main() {
    vec3 sum = vec3(0.0);
    
    sum += texture2D(sourceImage, textureCoordinate[0]).rgb * 0.018544;
    sum += texture2D(sourceImage, textureCoordinate[1]).rgb * 0.034167;
    sum += texture2D(sourceImage, textureCoordinate[2]).rgb * 0.056332;
    sum += texture2D(sourceImage, textureCoordinate[3]).rgb * 0.083109;
    sum += texture2D(sourceImage, textureCoordinate[4]).rgb * 0.109719;
    sum += texture2D(sourceImage, textureCoordinate[5]).rgb * 0.129618;
    sum += texture2D(sourceImage, textureCoordinate[6]).rgb * 0.137023;
    sum += texture2D(sourceImage, textureCoordinate[7]).rgb * 0.129618;
    sum += texture2D(sourceImage, textureCoordinate[8]).rgb * 0.109719;
    sum += texture2D(sourceImage, textureCoordinate[9]).rgb * 0.083109;
    sum += texture2D(sourceImage, textureCoordinate[10]).rgb * 0.056332;
    sum += texture2D(sourceImage, textureCoordinate[11]).rgb * 0.034167;
    sum += texture2D(sourceImage, textureCoordinate[12]).rgb * 0.018544;

    gl_FragColor = vec4(sum, 1.0);
}