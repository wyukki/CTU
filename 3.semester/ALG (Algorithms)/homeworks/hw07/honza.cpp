#include <algorithm>
#include <iomanip>
#include <iostream>
#include <vector>
using namespace std;

#define UNITIALIZED -1

struct coord_t {
    int col, height;
};

bool operator<(const coord_t &a, const coord_t &b) {
    return a.height > b.height || (a.height == b.height && a.col > b.col);
}

int get_biggest_profit(vector<coord_t> crds, int n_eggs, int wdth);

void print_coords(vector<coord_t> c, int eggs);
void print_cur_col(int col[], int wdth, int hght);

int main() {
    int res, width, num_of_eggs;
    cin >> width >> num_of_eggs;

    vector<coord_t> coords;
    for (int i = 0; i < num_of_eggs; i++) {
        coord_t c;
        cin >> c.col >> c.height;
        coords.push_back(c);
    }
    sort(coords.begin(), coords.end());
    cout << "sorted\n";

    // print_coords(coords, num_of_eggs);

    res = get_biggest_profit(coords, num_of_eggs, width);
    cout << res << "\n";
}

int get_biggest_profit(vector<coord_t> crds, int n_eggs, int wdth) {
    int ret = 0;
    int height_count = 0;
    bool cur_col = false;
    int cur_cols[2][wdth - 1];
    for (int i = 0; i < 2; i++) {
        for (int j = 0; j < wdth - 1; j++) {
            if (!i && !j)
                cur_cols[i][j] = 0;
            else
                cur_cols[i][j] = UNITIALIZED;
        }
    }
    coord_t cur_egg;
    while (!crds.empty()) {
        height_count++;

        for (int i = 0; i < wdth - 1; i++) {
            if (!i) {
                cur_cols[int(!cur_col)][i] = max(cur_cols[int(cur_col)][i],
                                                 cur_cols[int(cur_col)][i + 1]);
            } else if (i == wdth - 2) {
                cur_cols[int(!cur_col)][i] = max(cur_cols[int(cur_col)][i - 1],
                                                 cur_cols[int(cur_col)][i]);
            } else {
                cur_cols[int(!cur_col)][i] =
                    max(cur_cols[int(cur_col)][i - 1],
                        max(cur_cols[int(cur_col)][i],
                            cur_cols[int(cur_col)][i + 1]));
            }
        }

        while (crds.back().height == height_count) {
            if (crds.back().col < wdth &&
                cur_cols[int(!cur_col)][crds.back().col - 1] != UNITIALIZED) {
                cur_cols[int(!cur_col)][crds.back().col - 1]++;
            }
            if (crds.back().col > 1 &&
                cur_cols[int(!cur_col)][crds.back().col - 2] != UNITIALIZED) {
                cur_cols[int(!cur_col)][crds.back().col - 2]++;
            }
            crds.pop_back();
        }
        cur_col = !cur_col;
        print_cur_col(cur_cols[int(cur_col)], wdth - 1, height_count);
    }
    for (int i = 0; i < wdth - 1; i++) {
        if (cur_cols[int(cur_col)][i] > ret)
            ret = cur_cols[int(cur_col)][i];
    }
    return ret;
}

void print_coords(vector<coord_t> c, int eggs) {
    for (int i = 0; i < eggs; i++) {
        cout << c[i].height << " " << c[i].col << "\n";
    }
}

void print_cur_col(int col[], int wdth, int hght) {
    cout << setw(3) << hght << "| ";
    for (int i = 0; i < wdth; i++) {
        cout << setw(4) << col[i] << " ";
    }
    cout << "\n";
}
