import numpy as np
import cv2

def run(imgpath) :
    ########################################
    # 후보군 분류 ( 번호판 글자 크기로 추리기)
    MIN_AREA = 80  # 최소면적
    MIN_WIDTH, MIN_HEIGHT = 2, 8  # 최소 너비와 높이
    MIN_RATIO, MAX_RATIO = 0.25, 1.0  # 최소 최대 비율

    # 위치배열을 보고 번호판인지 후보군 추리기
    # 아래 값을 설정하여 후보군 명백화
    MAX_DIAG_MULTIPLYER = 5  # 컨투어 중심과 컨투어 중심의 사이가 대각선의 길이의 5배를 정함.
    MAX_ANGLE_DIFF = 12.0  # 12.0 기울여진 앵글의 맥스값.
    MAX_AREA_DIFF = 0.5  # 0.5 면적의차이 ( 원근법에 의해)
    MAX_WIDTH_DIFF = 0.8  # 위스 차이
    MAX_HEIGHT_DIFF = 0.2  # 높이차이
    MIN_N_MATCHED = 3  # 3 위조건을 3개 이상 매치되어야 그룹이라고 인정함

    # 로테이트 판 이미지 ( affine transform )
    PLATE_WIDTH_PADDING = 1.3  # 1.3
    PLATE_HEIGHT_PADDING = 1.5  # 1.5
    MIN_PLATE_RATIO = 3
    MAX_PLATE_RATIO = 10
    ##################################
    print(imgpath)
    img = cv2.imread(imgpath)
    height, width, channel = img.shape

    imgGray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    img_blur = cv2.GaussianBlur(imgGray, (5, 5), 0)
    img_thresh = cv2.adaptiveThreshold(
        img_blur,
        maxValue=255.0,
        adaptiveMethod=cv2.ADAPTIVE_THRESH_GAUSSIAN_C,
        thresholdType=cv2.THRESH_BINARY_INV,
        blockSize=19,
        C=9
    )

    contours, _ = cv2.findContours(
        img_thresh,
        mode=cv2.RETR_LIST,
        method=cv2.CHAIN_APPROX_SIMPLE
    )

    temp_result = np.zeros((height, width, channel), dtype=np.uint8)
    cv2.drawContours(temp_result, contours=contours, contourIdx=-1, color=(255, 255, 255))

    contours_dict = []
    for contour in contours:
        x, y, w, h = cv2.boundingRect(contour)
        cv2.rectangle(temp_result, pt1=(x, y), pt2=(x + w, y + h), color=(255, 255, 255), thickness=2)

        contours_dict.append({
            'contour': contour,
            'x': x,
            'y': y,
            'w': w,
            'h': h,
            'cx': x + (w / 2),
            'cy': y + (h / 2)
        })

    possible_contours = []
    cnt = 0

    for d in contours_dict:
        area = d['w'] * d['h']
        ratio = d['w'] / d['h']

        if area > MIN_AREA \
                and d['w'] > MIN_WIDTH and d['h'] > MIN_HEIGHT \
                and MIN_RATIO < ratio < MAX_RATIO:
            d['idx'] = cnt
            cnt += 1
            possible_contours.append(d)

    temp_result = np.zeros((height, width, channel), dtype=np.uint8)
    for d in possible_contours:
        cv2.rectangle(temp_result, pt1=(d['x'], d['y']), pt2=(d['x'] + d['w'], d['y'] + d['h']), color=(255, 255, 255),
                      thickness=2)


    def find_chars(contour_list):
        matched_result_idx = []

        for d1 in contour_list:
            matched_contours_idx = []
            for d2 in contour_list:
                if d1['idx'] == d2['idx']:  # 아이디가 같다는건 동일한 아이템이라는 뜻, 그래서 넘김
                    continue

                dx = abs(d1['cx'] - d2['cx'])  # 중심점 과 다른 중심점의 거리를 구함 = 즉 대각선 거리
                dy = abs(d1['cy'] - d2['cy'])

                diagonal_length1 = np.sqrt(d1['w'] ** 2 + d1['h'] ** 2)

                distance = np.linalg.norm(np.array([d1['cx'], d1['cy']]) - np.array([d2['cx'], d2['cy']]))
                if dx == 0:
                    angle_diff = 90  # 각도가 90은 직각임.
                else:  # 각도를 구하는 공식
                    angle_diff = np.degrees(np.arctan(dy / dx))

                # 면적의 비율, 너비의 비율, 높이의 비율
                area_diff = abs(d1['w'] * d1['h'] - d2['w'] * d2['h']) / (d1['w'] * d1['h'])
                width_diff = abs(d1['w'] - d2['w']) / d1['w']
                height_diff = abs(d1['h'] - d2['h']) / d1['h']

                # 우리가 정한 기준에 맞는 인덱스 값만 후보에 추가
                if distance < diagonal_length1 * MAX_DIAG_MULTIPLYER \
                        and angle_diff < MAX_ANGLE_DIFF and area_diff < MAX_AREA_DIFF \
                        and width_diff < MAX_WIDTH_DIFF and height_diff < MAX_HEIGHT_DIFF:
                    matched_contours_idx.append(d2['idx'])

            # append this contour
            matched_contours_idx.append(d1['idx'])  # 비교 대상이였던 D1 도 같이 넣어줌 .

            # 조건 중 3개 미만일경우 다음 포문으로 넘김.
            if len(matched_contours_idx) < MIN_N_MATCHED:
                continue

            matched_result_idx.append(matched_contours_idx)

            unmatched_contour_idx = []
            for d4 in contour_list:
                if d4['idx'] not in matched_contours_idx:
                    unmatched_contour_idx.append(d4['idx'])

            unmatched_contour = np.take(possible_contours, unmatched_contour_idx)

            recursive_contour_list = find_chars(unmatched_contour)

            for idx in recursive_contour_list:
                matched_result_idx.append(idx)  # 최종결과물

            break

        return matched_result_idx


    result_idx = find_chars(possible_contours)
    matched_result = []
    for idx_list in result_idx:
        matched_result.append(np.take(possible_contours, idx_list))

    # visualize possible contours
    temp_result = np.zeros((height, width, channel), dtype=np.uint8)

    for r in matched_result:
        for d in r:
            #         cv2.drawContours(temp_result, d['contour'], -1, (255, 255, 255))
            cv2.rectangle(temp_result, pt1=(d['x'], d['y']), pt2=(d['x'] + d['w'], d['y'] + d['h']),
                          color=(255, 255, 255),
                          thickness=2)

    plate_imgs = []
    plate_infos = []

    for i, matched_chars in enumerate(matched_result):
        sorted_chars = sorted(matched_chars, key=lambda x: x['cx'])  # 정렬 먼저함.

        # 센터x와 센터y를구함.
        plate_cx = (sorted_chars[0]['cx'] + sorted_chars[-1]['cx']) / 2
        plate_cy = (sorted_chars[0]['cy'] + sorted_chars[-1]['cy']) / 2

        plate_width = (sorted_chars[-1]['x'] + sorted_chars[-1]['w'] - sorted_chars[0]['x']) * PLATE_WIDTH_PADDING

        sum_height = 0
        for d in sorted_chars:
            sum_height += d['h']

        plate_height = int(sum_height / len(sorted_chars) * PLATE_HEIGHT_PADDING)

        triangle_height = sorted_chars[-1]['cy'] - sorted_chars[0]['cy']

        triangle_hypotenus = np.linalg.norm(
            np.array([sorted_chars[0]['cx'], sorted_chars[0]['cy']]) -
            np.array([sorted_chars[-1]['cx'], sorted_chars[-1]['cy']])
        )  # 첫번쨰 번호와 마지막 번호의 센터 두 점의 거리가 빗변임.

        # 각도 구함.
        angle = np.degrees(np.arcsin(triangle_height / triangle_hypotenus))

        # 로테이션 메트식스를 구함
        rotation_matrix = cv2.getRotationMatrix2D(center=(plate_cx, plate_cy), angle=angle, scale=1.0)

        # 그 값을 구해서 돌림
        img_rotated = cv2.warpAffine(img_thresh, M=rotation_matrix, dsize=(width, height))

        # 회전한 화면에서 번호판만 추출
        img_cropped = cv2.getRectSubPix(
            img_rotated,
            patchSize=(int(plate_width), int(plate_height)),
            center=(int(plate_cx), int(plate_cy))
        )

        if img_cropped.shape[1] / img_cropped.shape[0] < MIN_PLATE_RATIO or img_cropped.shape[1] / img_cropped.shape[
            0] < MIN_PLATE_RATIO > MAX_PLATE_RATIO:
            continue

        plate_imgs.append(img_cropped)
        plate_infos.append({
            'x': int(plate_cx - plate_width / 2),
            'y': int(plate_cy - plate_height / 2),
            'w': int(plate_width),
            'h': int(plate_height)
        })

        for i, plate_img in enumerate(plate_imgs):
            plate_img = cv2.resize(plate_img, dsize=(0, 0), fx=1.6, fy=1.6)
            _, plate_img = cv2.threshold(plate_img, thresh=0.0, maxval=255.0, type=cv2.THRESH_BINARY | cv2.THRESH_OTSU)

            # find contours again (same as above)
            contours, _ = cv2.findContours(plate_img, mode=cv2.RETR_LIST, method=cv2.CHAIN_APPROX_SIMPLE)

            plate_min_x, plate_min_y = plate_img.shape[1], plate_img.shape[0]
            plate_max_x, plate_max_y = 0, 0

            for contour in contours:
                x, y, w, h = cv2.boundingRect(contour)

                area = w * h
                ratio = w / h
                # 아까 설정한 기준에 맞는지 다시한번 확인
                if area > MIN_AREA \
                        and w > MIN_WIDTH and h > MIN_HEIGHT \
                        and MIN_RATIO < ratio < MAX_RATIO:
                    if x < plate_min_x:
                        plate_min_x = x
                    if y < plate_min_y:
                        plate_min_y = y
                    if x + w > plate_max_x:
                        plate_max_x = x + w
                    if y + h > plate_max_y:
                        plate_max_y = y + h
                # 위의 최소 최대값을 구하면 번호판의 크기를 구할 수 있음.

            img_result = plate_img[plate_min_y:plate_max_y, plate_min_x:plate_max_x]

            img_result = cv2.GaussianBlur(img_result, ksize=(3, 3), sigmaX=0)

            # 블러처리한거 스레싱
            _, img_result = cv2.threshold(img_result, thresh=0.0, maxval=255.0, type=cv2.THRESH_BINARY | cv2.THRESH_OTSU)

            # 여백을 주기
            img_result = cv2.copyMakeBorder(img_result, top=10, bottom=10, left=10, right=10,
                                            borderType=cv2.BORDER_CONSTANT,
                                            value=(0, 0, 0))

            cv2.imwrite(imgpath+'item1.jpg', img_result)
            print(imgpath+'item1.jpg')

            return imgpath+'item1.jpg'





