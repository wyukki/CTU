#include <math.h>
#include <stdio.h>
#include <stdlib.h>

unsigned char *read_data(const char *fname, int *width, int *height);
void free_memory(unsigned char *buffer, int width, int height);
void save_image(unsigned char *image, int width, int height, const char *output_fname);
unsigned char *sharp_image(unsigned char *image, int width, int height);

/**
 * Image sharpening with a certain mask.
 * Input: file name of image, output name of sharped image.
 * Full description of assignment: https://cw.fel.cvut.cz/b202/courses/b35apo/homeworks/02/start
 * Main taks was to implement algorithm, that will be effective in memory usage
 */

int main(int argc, char *argv[]) {
    int ret = EXIT_FAILURE;
    const char *fname = argc > 1 ? argv[1] : NULL;
    const char *output_fname = argc > 2 ? argv[2] : NULL;
    if (!fname || !output_fname) {
        fprintf(stderr, "Error: no input image!\n");
        return -1;
    }
    int width, height;
    unsigned char *image = read_data(fname, &width, &height);
    if (!image) {
        fprintf(stderr, "Image is not read!\n");
        return -2;
    }
    unsigned char *sharped_image = sharp_image(image, width, height);
    save_image(sharped_image, width, height, output_fname);
    free(image);
    free(sharped_image);
    return ret;
}
/**
 * Reads and returns image, represented by an array of unsigned chars
 * @arg fname - name of the file
 * @arg width - width of a image
 * @arg height - height of a image
 */
unsigned char *read_data(const char *fname, int *width, int *height) {
    FILE *f = fopen(fname, "rb");
    if (!f) {
        fprintf(stderr, "Error: open file on line %d\n", __LINE__);
        return NULL;
    }
    int brightness;
    if ((fscanf(f, "P6\n%d\n%d\n%d\n", width, height, &brightness)) <= 0) {
        fprintf(stderr, "Error: read first arguments of image on line %d!\n", __LINE__);
        return NULL;
    }
    unsigned char *img = (unsigned char *)malloc(*width * *height * 3);
    if ((fread(img, 1, *width * *height * 3, f)) != *width * *height * 3) {
        fprintf(stderr, "Error: fread from image on line %d\n", __LINE__);
        return NULL;
    }
    fclose(f);
    return img;
}
/**
 * Saves sharped image
 * @arg image - sharped image
 * @arg width - width of image
 * @arg height - height of image
 * @arg output_fname - name of output file
 */
void save_image(unsigned char *image, int width, int height, const char *output_fname) {
    FILE *out = fopen(output_fname, "wb");
    if (!out) {
        fprintf(stderr, "Error: open file on line %d\n", __LINE__);
        return;
    }
    int brightness = 255;
    fprintf(out, "P6\n%d\n%d\n%d\n", width, height, brightness);

    int ret = fwrite(image, 1, width * height * 3, out);
    if (ret != width * height * 3) {
        fprintf(stderr, "File saving error!\n");
    }
    fclose(out);
}
/**
 * Main function, recieves image and it's width and height.
 * Uses mask
 *   [0, -1,  0,
 *   -1, 5, -1
 *    0, -1, 0]
 * to sharp image.
 */
unsigned char *sharp_image(unsigned char *image, int width, int height) {
    unsigned char *sharped_image = malloc(sizeof(unsigned char) * width * height * 3);
    if (!sharped_image) {
        fprintf(stderr, "Allocation error on line %d!\n", __LINE__);
        return NULL;
    }
    register int y, x;
    // red value of image
    register int rValue = 0;
    // green value of image
    register int gValue = 0;
    // blue value of image
    register int bValue = 0;
    // counter of pixels brightness of sharped image, that will be printed in output txt
    register int brightness_to50 = 0;
    register int brightness_to100 = 0;
    register int brightness_to150 = 0;
    register int brightness_to200 = 0;
    register int brightness_to250 = 0;
    register double brightness = 0;
    for (y = 0; y < height; ++y) {
        for (x = 0; x < width; ++x) {
            if (y == 0 || x == 0 || x == width - 1 || y == height - 1) {
                // the "frame" of the image stays same, due to mask sizes
                rValue = image[(y * width + x) * 3];
                gValue = image[(y * width + x) * 3 + 1];
                bValue = image[(y * width + x) * 3 + 2];
                sharped_image[(y * width + x) * 3] = rValue;
                sharped_image[(y * width + x) * 3 + 1] = gValue;
                sharped_image[(y * width + x) * 3 + 2] = bValue;
            } else {
                // new colors value for sharped image
                rValue = (-1 * image[((y - 1) * width + x) * 3]) + (-1 * image[(y * width + (x - 1)) * 3]) + (5 * image[(y * width + x) * 3]) +
                         (-1 * image[(y * width + (x + 1)) * 3]) + (-1 * image[((y + 1) * width + x) * 3]);

                gValue = (-1 * image[((y - 1) * width + x) * 3 + 1]) + (-1 * image[(y * width + (x - 1)) * 3 + 1]) + (5 * image[(y * width + x) * 3 + 1]) +
                         (-1 * image[(y * width + (x + 1)) * 3 + 1]) + (-1 * image[((y + 1) * width + x) * 3 + 1]);

                bValue = (-1 * image[((y - 1) * width + x) * 3 + 2]) + (-1 * image[(y * width + (x - 1)) * 3 + 2]) + (5 * image[(y * width + x) * 3 + 2]) +
                         (-1 * image[(y * width + (x + 1)) * 3 + 2]) + (-1 * image[((y + 1) * width + x) * 3 + 2]);

                // checks if each color value is in interval [0, 255]

                if (rValue < 0) {
                    rValue = 0;
                }
                if (gValue < 0) {
                    gValue = 0;
                }
                if (bValue < 0) {
                    bValue = 0;
                }
                if (rValue > 255) {
                    rValue = 255;
                }
                if (gValue > 255) {
                    gValue = 255;
                }
                if (bValue > 255) {
                    bValue = 255;
                }
                sharped_image[(y * width + x) * 3] = rValue;
                sharped_image[(y * width + x) * 3 + 1] = gValue;
                sharped_image[(y * width + x) * 3 + 2] = bValue;
            }
            // computing brightness of pixel
            brightness = round(rValue * 0.2126 + gValue * 0.7152 + bValue * 0.0722);
            if (brightness <= 50) {
                brightness_to50++;
                continue;
            } else if (brightness >= 51 && brightness <= 101) {
                brightness_to100++;
                continue;
            } else if (brightness >= 102 && brightness <= 152) {
                brightness_to150++;
                continue;
            } else if (brightness >= 153 && brightness <= 203) {
                brightness_to200++;
                continue;
            } else if (brightness >= 204 && brightness <= 255) {
                brightness_to250++;
            }
        }
    }
    FILE *out_txt = fopen("output.txt", "w");
    if (!out_txt) {
        fprintf(stderr, "Error: open file on line %d\n", __LINE__);
        return NULL;
    }
    if ((fprintf(out_txt, "%d %d %d %d %d", brightness_to50, brightness_to100, brightness_to150, brightness_to200, brightness_to250)) <= 0) {
        fprintf(stderr, "Error: writing to txt file!\n");
        return NULL;
    }
    fclose(out_txt);
    return sharped_image;
}
