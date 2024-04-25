SUMMARY = "Linux kernel module"
DESCRIPTION = "The Linux kernel module that sets the watchpoint to a given memory address, and if this memory address is accessed then module's callbacks are called."
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=1ebbd3e34237af26da5dc08a4e440464"

inherit module

SRC_URI = "file://watchpoint.c \
           file://Makefile \
           file://COPYING \
          "

S= "${WORKDIR}"

# The inherit of module.bbclass will automatically name module packages with
# "kernel-module-" prefix as required by the oe-core build environment.

RPROVIDES_${PN} += "kernel-module-watchpoint"