# Twelve Plus

基于 **LineageOS Twelve** 的增强版音乐播放器，加入了 **FFmpeg ALAC 解码**、**M4A 内嵌同步歌词读取** 和 **本地 LRC 歌词支持**。

An enhanced fork of **LineageOS Twelve**, with **FFmpeg-based ALAC decoding**, **embedded synchronized lyrics support for M4A**, and **local LRC lyrics support**.

---

## 功能特性 / Features

### ALAC 播放支持

Twelve Plus 集成 Media3 FFmpeg decoder，为原版 Twelve 增加 ALAC 音频解码支持。

支持的典型格式：

- `.m4a` + ALAC
- Apple Lossless Audio Codec
- 本地 MediaStore 音频文件

FFmpeg 扩展解码器优先用于受支持的格式，其余格式仍可继续使用 Android MediaCodec。

### ALAC Playback

Twelve Plus integrates the Media3 FFmpeg decoder to add ALAC playback support to Twelve.

Typical supported use cases include:

- `.m4a` files encoded with ALAC
- Apple Lossless Audio Codec
- Local MediaStore audio files

The FFmpeg extension renderer is preferred where supported, while other formats can continue to use Android MediaCodec.

---

## 同步歌词 / Synchronized Lyrics

Twelve Plus 支持两种本地同步歌词来源。

### 1. M4A 内嵌歌词

优先读取 M4A 文件中的内嵌歌词 metadata。

目前支持 Media3 可解析为 `USLT` 的歌词 metadata，例如 MP4/M4A 中的 `©lyr` / lyrics metadata。

内嵌歌词可以直接采用标准 LRC 时间戳格式：

```text
[00:17.98]第一句歌词
[00:25.75]第二句歌词
[00:33.10]第三句歌词
```

不需要额外的 `.lrc` 文件。

### 2. 外置 LRC

如果音频文件没有可用的内嵌歌词，Twelve Plus 会自动查找同目录、同文件名的 `.lrc` 文件。

例如：

```text
Music/
├── Song.m4a
└── Song.lrc
```

同时兼容：

```text
Song.LRC
```

### Embedded Lyrics

Twelve Plus first attempts to read synchronized lyrics embedded directly in the M4A metadata.

Lyrics exposed by Media3 as `USLT` metadata are supported, including MP4/M4A lyrics metadata such as `©lyr`.

Embedded lyrics may contain standard LRC timestamps:

```text
[00:17.98]First lyric line
[00:25.75]Second lyric line
[00:33.10]Third lyric line
```

No external `.lrc` file is required.

### Sidecar LRC

If no usable embedded lyrics are found, Twelve Plus falls back to a sidecar `.lrc` file located next to the audio file.

Example:

```text
Music/
├── Song.m4a
└── Song.lrc
```

Uppercase `.LRC` files are also supported.

---

## 歌词交互 / Lyrics UI

Twelve 原有的同步歌词界面被保留并用于本地歌词。

支持：

- 当前歌词逐行高亮
- 自动跟随播放进度滚动
- 手动滚动后暂停自动跟随
- 一键返回当前播放歌词
- 点击某一行歌词跳转到对应时间
- 标准 LRC 毫秒时间戳
- LRC 全局 offset

Supported behavior includes:

- Active-line highlighting
- Automatic scrolling with playback
- Manual-scroll override
- Return-to-current-line control
- Tap a lyric line to seek
- Millisecond LRC timestamps
- Global LRC offset support

---

## LRC 格式 / LRC Format

支持：

```text
[03:12]Lyrics
[03:12.4]Lyrics
[03:12.45]Lyrics
[03:12.456]Lyrics
```

同一行多个时间戳：

```text
[01:00.00][02:00.00]Repeated lyrics
```

全局时间偏移：

```text
[offset:+500]
```

或者：

```text
[offset:-500]
```

单位为毫秒。

The offset value is expressed in milliseconds.

---

## 支持的 ABI / Supported ABIs

当前 Universal APK 包含：

- `arm64-v8a`
- `armeabi-v7a`
- `x86`
- `x86_64`

The current universal APK includes all four Android ABIs listed above.

---

## 下载 / Download

建议从 GitHub Releases 下载最新 APK：

```text
Twelve-Plus-universal.apk
```

The latest APK should be available from the GitHub Releases page.

---

## 安装 / Installation

Twelve Plus 使用独立的调试包名，因此可以与系统自带 Twelve 共存。

开发版本当前使用：

```text
org.lineageos.twelve.dev
```

通过 ADB 安装：

```bash
adb install -r Twelve-Plus-universal.apk
```

如果使用外置 `.lrc` 文件，当前实现可能需要完整文件访问权限：

```bash
adb shell appops set org.lineageos.twelve.dev MANAGE_EXTERNAL_STORAGE allow
```

之后重新启动应用即可。

The development build currently uses the package name:

```text
org.lineageos.twelve.dev
```

It can coexist with the system version of Twelve.

---

## 编译环境 / Build Environment

当前已验证的构建环境：

```text
JDK: 21
Android compileSdk: 36
Android NDK: 26.1.10909125
CMake: 3.22.1
Media3: 1.11.1
FFmpeg: 6.0
```

推荐使用 Linux 或 WSL2 构建。

### 环境变量

```bash
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
export PATH="$JAVA_HOME/bin:$PATH"

export ANDROID_HOME=$HOME/Android/Sdk
export ANDROID_SDK_ROOT=$ANDROID_HOME
export PATH="$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools:$PATH"
```

在项目根目录创建：

```text
local.properties
```

内容：

```text
sdk.dir=/home/your-user/Android/Sdk
```

---

## 编译 / Build

进入项目目录：

```bash
cd Twelve-Plus
```

构建 Debug APK：

```bash
./gradlew clean assembleDebug
```

输出：

```text
app/build/outputs/apk/debug/app-debug.apk
```

---

## FFmpeg

Twelve Plus 使用 AndroidX Media3 FFmpeg decoder extension。

FFmpeg 当前主要用于解决部分设备原生 Codec2 / MediaCodec 无法正确播放 ALAC 的问题。

本项目所包含的 FFmpeg native 库当前针对以下 ABI 构建：

```text
arm64-v8a
armeabi-v7a
x86
x86_64
```

The bundled FFmpeg decoder is currently focused on ALAC decoding rather than replacing the entire Android audio decoding stack.

---

## 实现逻辑 / Implementation Overview

播放链路：

```text
Audio file
    ↓
Media3 / ExoPlayer
    ↓
TwelveRenderersFactory
    ↓
FFmpeg extension renderer
    ↓
ALAC decoding
```

歌词链路：

```text
Audio URI
    ↓
Embedded metadata
    ↓
USLT / M4A lyrics
    ↓
LRC parser
    ↓
Lyrics model
```

如果没有内嵌歌词：

```text
Audio URI
    ↓
Audio file path
    ↓
Same-name .lrc / .LRC
    ↓
LRC parser
    ↓
Lyrics model
```

---

## 与原版 Twelve 的主要区别 / Differences from Twelve

Twelve Plus 当前主要增加：

- FFmpeg ALAC decoder
- M4A embedded synchronized lyrics
- Local sidecar LRC lyrics
- Local LRC parser
- Embedded-lyrics-first fallback logic
- Universal four-ABI FFmpeg build

原版 Twelve 的 UI、媒体库、播放控制、歌词界面以及主要架构均继续保留。

Twelve Plus intentionally keeps the original Twelve architecture and UI while extending local playback and lyrics functionality.

---

## 项目状态 / Project Status

当前版本已在实际 Android 设备上验证：

- ALAC `.m4a` 正常播放
- M4A 内嵌同步歌词正常读取
- `.lrc` 外置歌词正常读取
- 歌词自动滚动正常
- 当前行高亮正常
- 点击歌词跳转正常

Current tested status:

- ALAC M4A playback: working
- Embedded synchronized lyrics: working
- Sidecar LRC lyrics: working
- Auto-scroll: working
- Active-line highlighting: working
- Tap-to-seek: working

---

## 后续计划 / Roadmap

可能加入：

- 歌词时间偏移调节
- 双语歌词
- 歌词样式优化
- 当前行字体放大
- 更好的桌面歌词支持
- 独立正式包名
- Release 签名
- 更多 FFmpeg audio codec
- 更完整的歌词 metadata 支持

Possible future improvements:

- Adjustable lyric timing offset
- Bilingual lyrics
- Improved lyrics UI
- Enlarged active lyric line
- Floating / desktop lyrics
- Dedicated release package name
- Release signing
- Additional FFmpeg audio codecs
- Broader embedded metadata support

---

## 上游项目 / Upstream

Twelve Plus 基于 LineageOS Twelve 开发。

原始项目及其代码版权归相应作者和 LineageOS 项目所有。

Twelve Plus is a derivative project based on LineageOS Twelve.

This project does not claim authorship of the original Twelve application.

---

## Credits

感谢：

- LineageOS
- Twelve contributors
- AndroidX Media3
- FFmpeg
- Android Open Source Project

Thanks to:

- LineageOS
- Twelve contributors
- AndroidX Media3
- FFmpeg
- Android Open Source Project

---

## License

本项目继承并保留上游项目中的许可证与版权声明。

请勿删除源文件中的 SPDX 标记，例如：

```text
SPDX-FileCopyrightText: The LineageOS Project
SPDX-License-Identifier: Apache-2.0
```

FFmpeg 及其相关组件遵循 FFmpeg 自身对应的许可证条款。

新增代码应同时遵守仓库内现有许可证要求。

This repository preserves the original copyright and license notices from the upstream project.

FFmpeg components remain subject to their respective FFmpeg licensing terms.

Please refer to the license files included in this repository for details.

---

## Disclaimer

Twelve Plus 是社区衍生项目，与 LineageOS 官方没有直接隶属关系。

Twelve Plus is a community derivative project and is not an official LineageOS product.
