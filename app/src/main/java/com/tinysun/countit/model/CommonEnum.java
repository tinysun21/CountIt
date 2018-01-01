package com.tinysun.countit.model;

import com.tinysun.countit.R;

/**
 * Created by YS CHOI on 2018-01-01.
 */

public class CommonEnum {

    // 카운트 이미지
    public enum CountImageResource{

        COUNT_IMAGE_RESOURCE_SOLID_BLACK_1(R.mipmap.count_image_solid_black_1),
        COUNT_IMAGE_RESOURCE_SOLID_BLACK_2(R.mipmap.count_image_solid_black_2),
        COUNT_IMAGE_RESOURCE_SOLID_BLACK_3(R.mipmap.count_image_solid_black_3),
        COUNT_IMAGE_RESOURCE_SOLID_BLACK_4(R.mipmap.count_image_solid_black_4),
        COUNT_IMAGE_RESOURCE_SOLID_BLACK_5(R.mipmap.count_image_solid_black_5);

        private int imageResource;

        CountImageResource(int i) {
            imageResource = i;
        }

        public int getCountImageResource() {
            return imageResource;
        }
    }


    // 카운트 최대값
    public enum CountMaxValue{

        COUNT_MAX_VALUE(100);

        private int maxValue;

        CountMaxValue(int i) {
            maxValue = i;
        }

        public int getCountMaxValue() {
            return maxValue;
        }
    }

    // 카운트 1세트 카운트 수
    public enum CountNumOf1Set{

        COUNT_NUM_OF_1_SET(5);

        private int num;

        CountNumOf1Set(int i) {
            num = i;
        }

        public int getCountNumOf1Set() {
            return num;
        }
    }

    // 카운트 1줄 전체의 카운트 수
    public enum CountNumOf1Line{

        COUNT_NUM_OF_1_LINE(10);

        private int num;

        CountNumOf1Line(int i) {
            num = i;
        }

        public int getCountNumOf1Line() {
            return num;
        }
    }

    // 카운트 1줄 전체의 카운트 수
    public enum CountLayoutParams{

        RELATIVE_LAYOUT_TOP_MARGIN(16),
        IMAGE_VIEW_LEFT_MARGIN(24);

        private int margin;

        CountLayoutParams(int i) {
            margin = i;
        }

        public int getMargin() {
            return margin;
        }
    }

}
