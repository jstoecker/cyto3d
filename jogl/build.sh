#!/bin/bash

mkdir -p bin
jar cmf jogl_main.mf bin/jogl_main-2.1.0.jar gluegen-rt.jar jogl-all.jar native
jar cmf jogl_extension.mf bin/jogl_extension-2.1.0.jar
