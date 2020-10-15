﻿/*
* Copyright (c) 2015 Johannes Mittendorfer (http://johannes-mittendorfer.com)
* Licensed under the MIT License (LICENSE.txt).
*
* Version 2.2.2
* Build 2015-11-22
*/
var $ = null;
layui.define(['jquery'], function (exports) {

    $ = layui.jquery;
!function (a, b, c) { var d, e; return e = "EAN13", d = function () { function a(a, b, c) { var d; if (this.element = a, this.number = b, this.settings = { number: !0, prefix: !0, color: "#000", background: null, padding: 0, debug: !1, onValid: function () { }, onInvalid: function () { }, onSuccess: function () { }, onError: function () { } }, c) for (d in c) this.settings[d] = c[d]; this._name = e, this.init() } return a.prototype.settings = {}, a.prototype.init = function () { var a, b; return this.settings.number || (this.settings.prefix = !1), 12 === this.number.length && (a = this.generateCheckDigit(this.number), console.log(a), this.number += a), 13 === this.number.length ? (this.validate() ? this.settings.onValid.call() : this.settings.onInvalid.call(), b = this.getCode(), this.draw(b)) : this.settings.onError.call(this, "number length is not 12 or 13") }, a.prototype.getCode = function () { var a, b, c, d, e, f, g, h, i, j, k; for (k = [[13, 25, 19, 61, 35, 49, 47, 59, 55, 11], [39, 51, 27, 33, 29, 57, 5, 17, 9, 23], [114, 102, 108, 66, 92, 78, 80, 68, 72, 116]], a = [0, 11, 13, 14, 19, 25, 28, 21, 22, 26], e = 0, i = 0, g = parseInt(this.number.substr(0, 1), 10), c = a[g], h = this.number.substr(1), f = h.split(""), d = 0; 5 >= d;)j = c >> 5 - d & 1, e *= Math.pow(2, 7), b = parseInt(f[d], 10), e += k[j][b], d++; for (d = 0; 5 >= d;)i *= Math.pow(2, 7), b = parseInt(f[6 + d], 10), i += k[2][b], d++; return [e, i] }, a.prototype.clear = function (a) { return null === this.settings.background ? a.clearRect(0, 0, this.element.width, this.element.height) : (a.fillStyle = this.settings.background, a.fillRect(0, 0, this.element.width, this.element.height)) }, a.prototype.draw = function (a) { var b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z, A; if (j = { prefix_offset: .06, font_stretch: .073, border_line_height_number: .9, border_line_height: 1, line_height: .9, font_size: .15, font_y: 1.03, text_offset: 2 }, r = this.settings.prefix ? this.element.width - 2 * this.settings.padding - (this.element.width - 2 * this.settings.padding) * j.prefix_offset : this.element.width - 2 * this.settings.padding, this.settings.number ? (b = j.border_line_height_number * (this.element.height - 2 * this.settings.padding), f = j.line_height * b) : (b = j.border_line_height * (this.element.height - 2 * this.settings.padding), f = b), h = r / 95, this.element.getContext) { for (d = this.element.getContext("2d"), this.clear(d), d.fillStyle = this.settings.color, k = this.settings.number && this.settings.prefix ? this.element.width * j.prefix_offset + this.settings.padding : this.settings.padding, d.fillRect(k, this.settings.padding, h, b), k += 2 * h, d.fillRect(k, this.settings.padding, h, b), k += 7 * h * 6, g = 0; 42 >= g;)a[0] % 2 && d.fillRect(k, this.settings.padding, h, f), k -= h, a[0] = Math.floor(a[0] / 2), g++; for (k = k + 42 * h + 3 * h, d.fillRect(k, this.settings.padding, h, b), k += 2 * h, d.fillRect(k, this.settings.padding, h, b), k = k + 7 * h * 6 + h, n = 2199023255552; a[1] > 0;)a[1] % 2 && d.fillRect(k, this.settings.padding, h, f), k -= h, a[1] = Math.floor(a[1] / 2); if (k = k + 7 * h * 6 + h, d.fillRect(k, this.settings.padding, h, b), k += 2 * h, d.fillRect(k, this.settings.padding, h, b), this.settings.number) { for (d.font = j.font_size * f + "px monospace", p = this.number.substr(0, 1), this.settings.prefix && d.fillText(p, this.settings.padding, b * j.font_y + this.settings.padding), o = 3 * h + h * j.text_offset + (this.settings.prefix ? j.prefix_offset * this.element.width : 0) + this.settings.padding, c = this.number.substr(1, 6).split(""), i = t = 0, x = c.length; x > t; i = ++t)q = c[i], d.fillText(q, o, b * j.font_y + this.settings.padding), o += j.font_stretch * r; for (o = this.settings.padding + 50 * h + h * j.text_offset + (this.settings.prefix ? j.prefix_offset * this.element.width : 0), A = this.number.substr(7).split(""), i = u = 0, y = A.length; y > u; i = ++u)q = A[i], d.fillText(q, o, b * j.font_y + this.settings.padding), o += j.font_stretch * r } if (this.settings.debug) { for (e = [3, 10, 17, 24, 31, 38, 45], s = v = 0; h > 0 ? r >= v : v >= r; s = v += h)d.beginPath(), d.rect(s, 0, 1, b), d.fillStyle = "red", d.fill(); for (m = [3, 10, 17, 24, 31, 38, 45, 50, 57, 64, 71, 78, 85, 92], w = 0, z = m.length; z > w; w++)l = m[w], d.beginPath(), d.rect(l * h, 0, 1, this.element.height), d.fillStyle = "red", d.fill() } return this.settings.onSuccess.call() } return this.settings.onError.call(this, "canvas context is null") }, a.prototype.generateCheckDigit = function (a) { var b, c, d, e, f, g; for (c = 0, b = a.split(""), d = f = 0, g = b.length; g > f; d = ++f)e = b[d], c += d % 2 === 0 ? parseInt(e, 10) : 3 * parseInt(e, 10); return (10 - c % 10) % 10 }, a.prototype.validate = function () { return parseInt(this.number.slice(-1), 10) === this.generateCheckDigit(this.number.slice(0, -1)) }, a.prototype.toBin = function (a) { var b; return b = a.toString(2), "000000000".substr(b.length) + b }, a }(), a.fn[e] = function (b, c) { return this.each(function () { return a.data(this, "plugin_" + e, new d(this, b, c)) }) } }(jQuery, window, document);
function utf16to8(str) {
    var out, i, len, c;

    out = "";
    len = str.length;
    for (i = 0; i < len; i++) {
        c = str.charCodeAt(i);
        if ((c >= 0x0001) && (c <= 0x007F)) {
            out += str.charAt(i);
        } else if (c > 0x07FF) {
            out += String.fromCharCode(0xE0 | ((c >> 12) & 0x0F));
            out += String.fromCharCode(0x80 | ((c >> 6) & 0x3F));
            out += String.fromCharCode(0x80 | ((c >> 0) & 0x3F));
        } else {
            out += String.fromCharCode(0xC0 | ((c >> 6) & 0x1F));
            out += String.fromCharCode(0x80 | ((c >> 0) & 0x3F));
        }
    }
    return out;
}

function utf8to16(str) {
    var out, i, len, c;
    var char2, char3;

    out = "";
    len = str.length;
    i = 0;
    while (i < len) {
        c = str.charCodeAt(i++);
        switch (c >> 4) {
            case 0: case 1: case 2: case 3: case 4: case 5: case 6: case 7:
                // 0xxxxxxx
                out += str.charAt(i - 1);
                break;
            case 12: case 13:
                // 110x xxxx   10xx xxxx
                char2 = str.charCodeAt(i++);
                out += String.fromCharCode(((c & 0x1F) << 6) | (char2 & 0x3F));
                break;
            case 14:
                // 1110 xxxx  10xx xxxx  10xx xxxx
                char2 = str.charCodeAt(i++);
                char3 = str.charCodeAt(i++);
                out += String.fromCharCode(((c & 0x0F) << 12) |
                    ((char2 & 0x3F) << 6) |
                    ((char3 & 0x3F) << 0));
                break;
        }
    }

    return out;
}
exports('Ean', EAN13);

});