const modules = import.meta.glob<string>('../../assets/icons/*.svg', {
    eager: true,
    query: '?url',
    import: 'default',
})

export const icons = Object.fromEntries(
    Object.entries(modules).map(([filePath, url]) => {
        const name = filePath.split('/').pop()!.replace(/\.svg$/, '')
        return [name, url]
    }),
)

export type IconName = keyof typeof icons
