<h1 align="center">Linux kernel module</h1>

<h4 align="center">The Linux kernel module that sets the watchpoint to a given memory address, and if this memory address is accessed then module's callbacks are called.</h4>

## Video ##

## Pictures ##

## Project Overview ##

#### Functional requirements ####

1. Memory address to monitor is set by a module param and can be changed through sysfs module's entry.
2. The module sets watchpoint (hardware breakpoint) to a given memory address.
3. When the memory address is accessed (either read or write), then module's callbacks are called (separate for read and write) and a backtrace is printed.

#### Non-functional requirements ####

1. Linux kernel module is built by Yocto for qemux86.
2. Linux kernel module has its own Yocto recipe.

#### Expectations ####

1. Source code of Linux kernel module written according to the requirements.  Source code should be in a plain ASCII text (it is not a joke—sometimes we receive source code in MS Word document).
2. Yocto recipe for the kernel module.
3. Instruction how to build and run the module.
4. Description how you tested the kernel module.
5. Document whether your solution has additional (known to you) limitations or restrictions, or it has more features that it was requested.

## Usage Guide ##

#### Build an out-of-tree kernel module from scratch ####

1. First, you need to set up your build environment.  This includes installing necessary dependencies, such as `git`, `python`, `gcc`, and others.  On a Ubuntu system, you can do this with the following command:  
```bash
sudo apt-get install nano gawk wget lz4 git-core diffstat unzip texinfo gcc-multilib build-essential chrpath socat cpio python python3 python3-pip python3-pexpect xz-utils debianutils iputils-ping python3-git python3-jinja2 libegl1-mesa libsdl1.2-dev pylint3 xterm
```  
2. Poky is the reference system of the Yocto Project, which includes OpenEmbedded Core, BitBake, and other essential metadata.  You can clone the Poky repository with `git`.  Open a terminal window and type the command shown below.  Please make sure that the path to the directory doesn't include spaces; otherwise, Poky will issue lots of errors later on.  It's best to use a home directory, i.e., `~`.  Also, keep in mind that Poky's size is about 200MB.  
```bash
git clone https://git.yoctoproject.org/git/poky
```  
3. The previous command will create folder `poky`.  Move into that directory:  
```bash
cd ~/poky/
```  
4. You need to `source` the `oe-init-build-env` script located in the Poky directory to set up the environment variables necessary for the build.  This will move you into the `~/poky/build/` directory:  
```bash
source oe-init-build-env
```  
5. Please make sure locale `en_US.UTF-8` is available on your system; otherwise, you'll get errors.  
```bash
sudo dpkg-reconfigure locales
```  
6. Create a new layer for your kernel module named `meta-watchpoint-yocto` (you can't name it however you want to) to keep it separate from the rest of your Yocto project.  You can do this using the `bitbake-layers` command.  This will create directory: `~/poky/meta-watchpoint-yocto/`.  From inside the `~/poky/build/` directory:  
```bash
bitbake-layers create-layer ../meta-watchpoint-yocto
```  
7. Add your new layer to the build-system.  This should automatically update the `bblayers.conf` file in `~/poky/build/conf/`.  You should be in the`~/poky/build/` directory; if not, `cd ~/poky/build/`.  
```bass
bitbake-layers add-layer ../meta-watchpoint-yocto
```  
8. Move into the module's directory:  
```bash
cd ~/poky/meta-watchpoint-yocto/
```  
9. Remove the `recipes-example` directory:  
```bash
rm -rf recipes-example/
```  
10. Create a directory, e.g., `recipes-kernel`:  
```bash
mkdir recipes-kernel
```  
11. Move into that directory:  
```bash
cd ~/poky/meta-watchpoint-yocto/recipes-kernel/
```  
12. Make a directory `watchpoint-mod` (because of naming conventions, you can't name it however you want):  
```bash
mkdir watchpoint-mod
```  
13. Move there:  
```bash
cd ~/poky/meta-watchpoint-yocto/recipes-kernel/watchpoint-mod/
```  
14. Create a new file, say `kernel-module-watchpoint_0.1.bb`:  
```bash
touch kernel-module-watchpoint_0.1.bb
```  
15. Open the file:  
```bash
nano kernel-module-watchpoint_0.1.bb
```  
16. Add the following content to that file:  
```
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
```  
17. Make a folder `files`:  
```bash
mkdir files
```  
18. Move into that directory:  
```bash
cd ~/poky/meta-watchpoint-yocto/recipes-kernel/watchpoint-mod/files/
```  
19. Create a file named `watchpoint.c`:  
```bash
touch watchpoint.c
```  
20. Open the file:  
```bash
nano watchpoint.c
```  
21. Add the following content to the file:  
```c
#include <linux/module.h>
int init_module(void) {
  printk("Hello, World!\n");
  return 0;
}
void cleanup_module(void) {
  printk("Goodbye, World!\n");
}
MODULE_LICENSE("GPL");
```  
22. Optionally, create (or copy) a file named `COPYING` with the GNU General Public License.  
23. Create the `Makefile` file.  The file's name has to start with a capital letter; otherwise, you'll get errors in later steps.  
```bash
touch Makefile
```  
24. Open it:  
```bash
nano Makefile
```  
25. Add the following to the `Makefile`:  
```makefile
obj-m := watchpoint.o
SRC := $(shell pwd)
.PHONY:all
all:
	$(MAKE) -C $(KERNEL_SRC) M=$(SRC)
.PHONY:modules_install
modules_install:
	$(MAKE) -C $(KERNEL_SRC) M=$(SRC) modules_install
.PHONY:clean
clean:
	rm -f *.o *~ core .depend .*.cmd *.ko *.mod.c
	rm -f Module.markers Module.symvers modules.order
	rm -rf .tmp_versions Modules.symvers
```  
26. Inside the directory `~/poky/meta-watchpoint-yocto/`, the tree should look like this:  
```
.
├── conf
│   └── layer.conf
├── COPYING.MIT
├── README
└── recipes-kernel
    └── watchpoint-mod
        ├── files
        │   ├── COPYING
        │   ├── watchpoint.c
        │   └── Makefile
        └── kernel-module-watchpoint_0.1.bb
```  
27. Move into the `build` directory:  
```bash
cd ~/poky/build/
```  
28. Recheck the layers.  It should show `meta-watchpoint-yocto  /home/danika/poky/meta-watchpoint-yocto  6` in the last line of the output of the following command:  
```bash
bitbake-layers show-layers
```  
29. Update the build configuration:  
```bash
echo 'MACHINE_ESSENTIAL_EXTRA_DEPENDS += "kernel-module-watchpoint"' >> ~/poky/build/conf/local.conf
```  
30. From inside the `~/poky/build/` directory, compile the code.  Be aware, this step is very time consuming.  For example, the compilation of version 2.9.0 took 2 hours minutes on a laptop with a 2-core CPU, 16GB of RAM, and HDD.  Also, keep in mind that after the operation finishes the `~/poky/` directory will have the size of 26GB.  
```bash
bitbake kernel-module-watchpoint
```  
31. Generate an index of the packages that have been built.  On the same machine, this step took 10 minutes.  
```bash
bitbake package-index
```  
32. Finally, build your image using the `bitbake` command.  The `core-image-sato` is an image with Sato support, a mobile environment and visual style that works well with mobile devices.  The image supports X11 with a Sato theme and applications such as a terminal, editor, file manager, media player, and so forth.  The resulting image will be located in the `~/poky/build/tmp/deploy/images/` directory.  This step is lengthy too—on the same machine, it took 14:59 UNFINISHED hours minutes.  The `~/poky/` directory will have the size of GB.  
```bash
bitbake core-image-sato
```  
33. Run the generated image in QEMU with the command:  
```bash
runqemu qemux86-64
```  
34. Test the module:  
```bash
modprobe watchpoint
```

## License ##

GPL-3.0-or-later
